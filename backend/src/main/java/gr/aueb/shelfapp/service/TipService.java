package gr.aueb.shelfapp.service;

import gr.aueb.shelfapp.dto.TipDto;
import gr.aueb.shelfapp.entity.Category;
import gr.aueb.shelfapp.repository.CategoryRepository;
import java.util.List;
import java.util.Map;
import java.util.Random;
import org.springframework.stereotype.Service;

/**
 * Feature #3 from the project idea: a short motivational message about why
 * a category of food matters, so using it up (instead of wasting it) feels
 * like a small win rather than a chore.
 *
 * Unlike Product/Donation/WasteLog etc, these tips are curated app content,
 * not something a user creates or that needs its own database table - so
 * they're simply seeded here in code, matched by category name.
 */
@Service
public class TipService {

    private static final Map<String, List<String>> TIPS_BY_CATEGORY = Map.of(
            "Dairy", List.of(
                    "Milk and yogurt are packed with calcium and protein - great for bones and muscles!",
                    "A little cheese goes a long way: it's calorie-dense, so using it up is easy on your wallet too."
            ),
            "Fruits", List.of(
                    "Fruit is full of vitamins and fiber - eating it before it turns keeps both you and your budget happy.",
                    "Overripe fruit is perfect for a smoothie - nothing needs to go to waste!"
            ),
            "Vegetables", List.of(
                    "Vegetables are some of the most nutrient-dense food there is - don't let them sit forgotten in the fridge.",
                    "A wilting vegetable is still great in a soup or stir-fry - texture matters less once it's cooked."
            ),
            "Bakery", List.of(
                    "Stale bread makes excellent croutons or breadcrumbs - it doesn't have to be thrown away.",
                    "Bread freezes really well - if you won't finish it in time, freeze it today instead of wasting it later."
            ),
            "Meat", List.of(
                    "Meat has a real environmental and financial cost - using it before it spoils really matters.",
                    "Not sure you'll cook it in time? Freezing it early is an easy way to stop the clock on spoilage."
            )
    );

    private static final List<String> GENERAL_TIPS = List.of(
            "Food waste is one of the easiest ways to waste money without noticing it - every saved product counts!",
            "Around a third of all food produced worldwide is wasted - using up what you already have really helps.",
            "Planning meals around what's about to expire is one of the simplest habits that actually saves money.",
            "A quick weekly check of your pantry takes two minutes and can save you from tossing out forgotten items.",
            "Rotting food in landfills produces methane, a potent greenhouse gas - using it up helps the planet too.",
            "\"First in, first out\": move older items to the front of the shelf so they get used before newer ones.",
            "Buying only what you'll realistically use is the single biggest lever against food waste at home.",
            "Not sure if something's still good? Trust your senses - many foods are fine well past their printed date.",
            "Leftovers count too - a small container of yesterday's dinner is one less product wasted today.",
            "Batch-cooking food that's about to expire and freezing portions is an easy way to stop the clock on it."
    );

    private final CategoryRepository categoryRepository;
    private final Random random = new Random();

    public TipService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    /** categoryId is optional - null (or an unrecognized category) falls back to a general tip. */
    public TipDto randomTip(Long categoryId) {
        Pool resolved = resolvePool(categoryId);
        String message = resolved.pool().get(random.nextInt(resolved.pool().size()));
        return new TipDto(resolved.categoryName(), message);
    }

    /**
     * The whole set of tips for a category (or the general set, if categoryId
     * is null/unrecognized) - so the frontend can cycle through all of them
     * without repeating one until every tip in the set has been shown.
     */
    public List<TipDto> allTips(Long categoryId) {
        Pool resolved = resolvePool(categoryId);
        return resolved.pool().stream()
                .map(message -> new TipDto(resolved.categoryName(), message))
                .toList();
    }

    private Pool resolvePool(Long categoryId) {
        String categoryName = null;
        List<String> pool = GENERAL_TIPS;

        if (categoryId != null) {
            categoryName = categoryRepository.findById(categoryId)
                    .map(Category::getName)
                    .orElse(null);

            List<String> categoryTips = categoryName != null ? TIPS_BY_CATEGORY.get(categoryName) : null;
            if (categoryTips != null && !categoryTips.isEmpty()) {
                pool = categoryTips;
            }
        }

        return new Pool(categoryName, pool);
    }

    private record Pool(String categoryName, List<String> pool) {
    }
}
