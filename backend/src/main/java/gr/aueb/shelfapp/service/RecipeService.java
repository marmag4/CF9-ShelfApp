package gr.aueb.shelfapp.service;

import gr.aueb.shelfapp.dto.RecipeDto;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import org.springframework.stereotype.Service;

@Service
public class RecipeService {

    /** A handful of recipes that fit any product whose name contains one of these keywords. */
    private record ProductRecipes(List<String> keywords, List<RecipeDto> recipes) {
        boolean matches(String productNameLowerCase) {
            return keywords.stream().anyMatch(productNameLowerCase::contains);
        }
    }

    /** Product-specific groups first, then a category-wide fallback for anything that matches none of them. */
    private record CategoryRecipes(List<ProductRecipes> byProduct, List<RecipeDto> fallback) {
    }

    private static final Map<String, CategoryRecipes> RECIPES_BY_CATEGORY = Map.of(
            "Dairy", new CategoryRecipes(
                    List.of(
                            new ProductRecipes(
                                    List.of("milk"),
                                    List.of(
                                            new RecipeDto(
                                                    "Cheese & Milk Bechamel Sauce",
                                                    "Turns milk into a base for pasta bakes or lasagna.",
                                                    List.of("milk", "cheese (optional)", "butter", "flour"),
                                                    "Melt butter, whisk in flour, slowly add milk while stirring until it thickens, then stir in cheese if using."
                                            ),
                                            new RecipeDto(
                                                    "Warm Spiced Milk",
                                                    "A cozy way to use up milk that's close to its date, no cooking skills needed.",
                                                    List.of("milk", "cinnamon or cocoa", "honey (optional)"),
                                                    "Warm the milk gently on the stove (don't boil), stir in cinnamon or cocoa and honey to taste."
                                            )
                                    )
                            ),
                            new ProductRecipes(
                                    List.of("yogurt", "yoghurt"),
                                    List.of(
                                            new RecipeDto(
                                                    "Quick Yogurt Parfait",
                                                    "A simple layered snack that uses up yogurt before it turns.",
                                                    List.of("yogurt", "honey or jam", "any leftover fruit", "a handful of oats or granola"),
                                                    "Layer yogurt, fruit, and oats in a glass. Drizzle with honey. Ready in 5 minutes."
                                            ),
                                            new RecipeDto(
                                                    "Yogurt & Garlic Dip",
                                                    "A tzatziki-style dip that uses up a tub of yogurt in one go.",
                                                    List.of("yogurt", "garlic", "cucumber (optional)", "salt", "olive oil"),
                                                    "Mix yogurt with crushed garlic, grated cucumber if using, a pinch of salt, and a drizzle of olive oil."
                                            )
                                    )
                            ),
                            new ProductRecipes(
                                    List.of("cheese"),
                                    List.of(
                                            new RecipeDto(
                                                    "Grilled Cheese Toast",
                                                    "The fastest way to use up cheese that's starting to dry out.",
                                                    List.of("cheese", "bread", "butter"),
                                                    "Butter two slices of bread, add cheese in between, and fry in a pan until golden on both sides."
                                            ),
                                            new RecipeDto(
                                                    "Cheese Quesadilla",
                                                    "Uses up a good amount of cheese at once, with whatever else you have on hand.",
                                                    List.of("cheese", "tortilla or flatbread", "any leftover vegetables (optional)"),
                                                    "Sprinkle cheese (and vegetables, if using) over a tortilla, fold in half, and fry until the cheese melts."
                                            )
                                    )
                            )
                    ),
                    List.of(
                            new RecipeDto(
                                    "Simple Dairy Bake",
                                    "A flexible way to use up any dairy product before it expires - butter, cream, or leftovers not covered above.",
                                    List.of("any dairy product you have", "bread or pasta", "salt and pepper"),
                                    "Combine the dairy product with bread or pasta in an oven dish, season, and bake at 180C for about 15-20 minutes until golden."
                            )
                    )
            ),
            "Fruits", new CategoryRecipes(
                    List.of(
                            new ProductRecipes(
                                    List.of("banana"),
                                    List.of(
                                            new RecipeDto(
                                                    "Banana Pancakes",
                                                    "A great way to use up bananas that have gone too soft to eat plain.",
                                                    List.of("ripe banana", "egg", "a bit of flour or oats"),
                                                    "Mash the banana, mix with egg and flour/oats, then fry small spoonfuls in a pan until golden on both sides."
                                            ),
                                            new RecipeDto(
                                                    "No-Waste Banana Bread",
                                                    "The classic fix for overripe bananas - the riper, the better.",
                                                    List.of("ripe bananas", "flour", "sugar", "egg", "baking soda"),
                                                    "Mash the bananas, mix with the other ingredients into a batter, pour into a loaf tin, and bake at 180C for about 45 minutes."
                                            )
                                    )
                            ),
                            new ProductRecipes(
                                    List.of("apple"),
                                    List.of(
                                            new RecipeDto(
                                                    "Baked Apples",
                                                    "Turns apples that are going soft into a warm dessert.",
                                                    List.of("apples", "cinnamon", "honey or sugar", "a handful of nuts or raisins (optional)"),
                                                    "Core the apples, fill with cinnamon, honey, and nuts if using, then bake at 180C for 25-30 minutes."
                                            ),
                                            new RecipeDto(
                                                    "Quick Apple Sauce",
                                                    "Uses up several apples at once and keeps for a few extra days.",
                                                    List.of("apples, chopped", "a splash of water", "a spoon of sugar (optional)"),
                                                    "Simmer chopped apples with a splash of water for 10-15 minutes until soft, then mash to your liking."
                                            )
                                    )
                            )
                    ),
                    List.of(
                            new RecipeDto(
                                    "Overripe Fruit Smoothie",
                                    "The classic fix for fruit that's gone a bit too soft to eat as-is.",
                                    List.of("any soft/overripe fruit", "milk or yogurt", "a spoon of honey (optional)"),
                                    "Blend everything together until smooth. Add ice if you want it colder."
                            ),
                            new RecipeDto(
                                    "Simple Stewed Fruit",
                                    "Softens fruit further and makes it last a few more days as a topping.",
                                    List.of("fruit, chopped", "a splash of water", "a spoon of sugar (optional)"),
                                    "Simmer chopped fruit with a splash of water for 5-10 minutes until soft. Great on yogurt, oats, or toast."
                            )
                    )
            ),
            "Vegetables", new CategoryRecipes(
                    List.of(
                            new ProductRecipes(
                                    List.of("tomato"),
                                    List.of(
                                            new RecipeDto(
                                                    "Quick Tomato Sauce",
                                                    "Uses up tomatoes that have gone very soft - perfect for sauce, not so much for salad.",
                                                    List.of("tomatoes", "garlic", "olive oil", "salt and basil (optional)"),
                                                    "Saute garlic in olive oil, add chopped tomatoes, simmer for 15-20 minutes, season with salt and basil."
                                            ),
                                            new RecipeDto(
                                                    "Simple Bruschetta",
                                                    "A quick way to use a few tomatoes before they turn.",
                                                    List.of("tomatoes", "bread", "garlic", "olive oil", "basil (optional)"),
                                                    "Dice the tomatoes and mix with olive oil, garlic, and basil. Spoon over toasted bread."
                                            )
                                    )
                            ),
                            new ProductRecipes(
                                    List.of("potato"),
                                    List.of(
                                            new RecipeDto(
                                                    "Simple Mashed Potatoes",
                                                    "A reliable way to use up potatoes that are starting to sprout.",
                                                    List.of("potatoes", "butter or oil", "milk (optional)", "salt and pepper"),
                                                    "Boil the potatoes until soft, drain, then mash with butter, a splash of milk, salt, and pepper."
                                            ),
                                            new RecipeDto(
                                                    "Roasted Potato Wedges",
                                                    "Uses up a good amount of potatoes at once, with minimal effort.",
                                                    List.of("potatoes, cut into wedges", "oil", "salt", "any dried herbs (optional)"),
                                                    "Toss the wedges with oil, salt, and herbs, then roast at 200C for 25-30 minutes, turning halfway."
                                            )
                                    )
                            )
                    ),
                    List.of(
                            new RecipeDto(
                                    "Anything-Goes Stir-Fry",
                                    "Uses up vegetables that are starting to wilt, no matter which ones you have.",
                                    List.of("any vegetables you have", "garlic", "oil", "soy sauce"),
                                    "Chop the vegetables, fry garlic in oil, add the vegetables and stir-fry for 5-7 minutes, season with soy sauce."
                            ),
                            new RecipeDto(
                                    "Leftover Vegetable Soup",
                                    "A forgiving recipe - almost any vegetable works here.",
                                    List.of("any vegetables you have", "stock or water", "an onion", "salt and pepper"),
                                    "Saute the onion, add chopped vegetables and stock, simmer for 20 minutes, then blend if you like it smooth."
                            )
                    )
            ),
            "Bakery", new CategoryRecipes(
                    List.of(
                            new ProductRecipes(
                                    List.of("bread"),
                                    List.of(
                                            new RecipeDto(
                                                    "Simple Bread Croutons",
                                                    "Turns stale bread into something crunchy and useful instead of throwing it away.",
                                                    List.of("stale bread", "olive oil", "salt", "any dried herbs (optional)"),
                                                    "Cube the bread, toss with olive oil and salt, bake at 180C for 10-12 minutes until golden."
                                            ),
                                            new RecipeDto(
                                                    "Quick French Toast",
                                                    "A classic way to use bread that's a bit too dry to eat plain.",
                                                    List.of("bread", "egg", "milk", "a pinch of sugar or cinnamon"),
                                                    "Whisk the egg with milk and sugar, dip the bread slices, fry in a pan until golden on both sides."
                                            )
                                    )
                            )
                    ),
                    List.of(
                            new RecipeDto(
                                    "Bread & Butter Pudding",
                                    "A flexible way to use up any bakery item nearing its date - rolls, buns, or slices alike.",
                                    List.of("any bakery item, torn into pieces", "egg", "milk", "a spoon of sugar"),
                                    "Layer the torn pieces in a dish, whisk egg with milk and sugar, pour over, and bake at 180C for about 25 minutes."
                            )
                    )
            ),
            "Meat", new CategoryRecipes(
                    List.of(
                            new ProductRecipes(
                                    List.of("chicken"),
                                    List.of(
                                            new RecipeDto(
                                                    "One-Pan Chicken & Vegetables",
                                                    "A straightforward way to cook chicken before it needs to be thrown out.",
                                                    List.of("chicken", "any vegetables you have", "oil", "salt and pepper"),
                                                    "Season the chicken, sear it in a hot pan, add chopped vegetables, and cook everything together until the chicken is done."
                                            ),
                                            new RecipeDto(
                                                    "Simple Chicken Soup",
                                                    "A comforting way to stretch chicken further and use it up in one go.",
                                                    List.of("chicken", "stock or water", "any vegetables you have", "salt and pepper"),
                                                    "Simmer the chicken in stock with chopped vegetables for 20-30 minutes until cooked through, then season to taste."
                                            )
                                    )
                            )
                    ),
                    List.of(
                            new RecipeDto(
                                    "One-Pan Meat & Vegetables",
                                    "A straightforward way to cook meat before it needs to be thrown out.",
                                    List.of("meat", "any vegetables you have", "oil", "salt and pepper"),
                                    "Season the meat, sear it in a hot pan, add chopped vegetables, and cook everything together until done."
                            )
                    )
            )
    );

    /**
     * Recipes for a specific product: tries to match the product's own name
     * against the category's known products first (e.g. "milk" within
     * Dairy), and only falls back to the category-wide generic recipes if
     * nothing more specific matches. Unknown/unmatched category -> empty
     * list, rather than an error.
     */
    public List<RecipeDto> forProduct(String categoryName, String productName) {
        CategoryRecipes category = RECIPES_BY_CATEGORY.get(categoryName);
        if (category == null) {
            return List.of();
        }

        String nameLowerCase = productName == null ? "" : productName.toLowerCase(Locale.ROOT);
        List<RecipeDto> specific = category.byProduct().stream()
                .filter(group -> group.matches(nameLowerCase))
                .flatMap(group -> group.recipes().stream())
                .toList();

        return specific.isEmpty() ? category.fallback() : specific;
    }
}
