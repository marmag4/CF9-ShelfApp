import { useEffect, useRef, useState } from "react";
import apiClient from "../api/client";

/** Fisher-Yates shuffle - returns a new shuffled array, doesn't mutate the input. */
function shuffle(array) {
  const result = [...array];
  for (let i = result.length - 1; i > 0; i--) {
    const j = Math.floor(Math.random() * (i + 1));
    [result[i], result[j]] = [result[j], result[i]];
  }
  return result;
}

/**
 * Feature #3 from the project idea: a small motivating message, so using
 * up your food (instead of wasting it) feels a bit more rewarding.
 *
 * Loads the whole set of tips for the category (GET /api/tips/all) once,
 * then hands them out one at a time in shuffled order on "New tip" - so
 * the same tip never shows twice in a row until every tip in the set has
 * been shown, at which point the set is reshuffled and handed out again.
 */
export default function TipWidget({ categoryId } = {}) {
  const [tip, setTip] = useState(null);
  const [loading, setLoading] = useState(true);

  // The full set of tips for this category, fetched once. Kept in a ref
  // since it never changes at runtime and reading it shouldn't trigger a
  // re-render.
  const poolRef = useRef([]);
  // The still-untold tips from the current shuffled round.
  const queueRef = useRef([]);

  const showNextTip = () => {
    const pool = poolRef.current;
    if (pool.length === 0) return;

    if (queueRef.current.length === 0) {
      // Every tip in the set has been shown - start a fresh shuffled round.
      // If there's more than one tip, avoid immediately repeating the one
      // just shown so back-to-back clicks don't feel like nothing changed.
      let reshuffled = shuffle(pool);
      if (pool.length > 1 && reshuffled[0].message === tip?.message) {
        reshuffled = [...reshuffled.slice(1), reshuffled[0]];
      }
      queueRef.current = reshuffled;
    }

    const [next, ...rest] = queueRef.current;
    queueRef.current = rest;
    setTip(next);
  };

  useEffect(() => {
    // Guards against React StrictMode's double-invoked effects in dev mode
    // (mount -> cleanup -> mount again): without this, the first request's
    // response can land after the second and stomp on a round already in
    // progress, which is what made it look like tips were repeating.
    let cancelled = false;

    setLoading(true);
    apiClient
      .get("/tips/all", { params: categoryId ? { categoryId } : {} })
      .then((response) => {
        if (cancelled) return;
        poolRef.current = response.data;
        queueRef.current = [];
        showNextTip();
      })
      .finally(() => {
        if (!cancelled) setLoading(false);
      });

    return () => {
      cancelled = true;
    };
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [categoryId]);

  return (
    <div className="card tip-box">
      {loading || !tip ? (
        <span>Loading a tip...</span>
      ) : (
        <span>
          💡{" "}
          {/* General tips (no specific product category) come back with category: null - just show the message then. */}
          {tip.category && <strong>{tip.category}: </strong>}
          {tip.message}
        </span>
      )}
      <button className="btn btn-sm" onClick={showNextTip} disabled={loading}>
        New tip
      </button>
    </div>
  );
}
