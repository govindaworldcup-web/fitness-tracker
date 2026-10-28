package com.example.data.local

import com.example.data.model.FoodItem

object FoodCatalog {
    val categories = listOf(
        "All",
        "Fruits & Berries",
        "Vegetables",
        "Grains & Bakery",
        "Protein & Meat",
        "Dairy & Eggs",
        "Nuts & Healthy Fats",
        "Meals & Snacks"
    )

    val sampleFoods = listOf(
        // Fruits
        FoodItem("f_apple", "Apple (Fresh)", "Fruits & Berries", 52.0, 0.3, 13.8, 0.2, 180.0, "1 medium apple (180g)"),
        FoodItem("f_banana", "Banana", "Fruits & Berries", 89.0, 1.1, 22.8, 0.3, 120.0, "1 medium banana (120g)"),
        FoodItem("f_orange", "Orange", "Fruits & Berries", 47.0, 0.9, 11.8, 0.1, 140.0, "1 medium orange (140g)"),
        FoodItem("f_strawberries", "Strawberries", "Fruits & Berries", 32.0, 0.7, 7.7, 0.3, 150.0, "1 cup sliced (150g)"),
        FoodItem("f_blueberries", "Blueberries", "Fruits & Berries", 57.0, 0.7, 14.5, 0.3, 148.0, "1 cup fresh (148g)"),
        FoodItem("f_avocado", "Avocado", "Fruits & Berries", 160.0, 2.0, 8.5, 14.7, 150.0, "1 whole avocado (150g)"),
        FoodItem("f_grapes", "Grapes", "Fruits & Berries", 69.0, 0.7, 18.1, 0.2, 100.0, "1 small bunch (100g)"),
        FoodItem("f_watermelon", "Watermelon", "Fruits & Berries", 30.0, 0.6, 7.6, 0.2, 280.0, "1 generous wedge (280g)"),
        FoodItem("f_mango", "Mango", "Fruits & Berries", 60.0, 0.8, 15.0, 0.4, 200.0, "1 cup sliced (200g)"),
        FoodItem("f_kiwi", "Kiwi Fruit", "Fruits & Berries", 61.0, 1.1, 14.7, 0.5, 75.0, "1 kiwi (75g)"),

        // Vegetables
        FoodItem("v_broccoli", "Broccoli (Steamed)", "Vegetables", 35.0, 2.4, 7.2, 0.4, 150.0, "1 cup florets (150g)"),
        FoodItem("v_spinach", "Fresh Spinach", "Vegetables", 23.0, 2.9, 3.6, 0.4, 100.0, "3 cups raw (100g)"),
        FoodItem("v_carrot", "Carrots", "Vegetables", 41.0, 0.9, 9.6, 0.2, 80.0, "1 medium carrot (80g)"),
        FoodItem("v_tomato", "Tomatoes", "Vegetables", 18.0, 0.9, 3.9, 0.2, 120.0, "1 medium tomato (120g)"),
        FoodItem("v_cucumber", "Cucumber", "Vegetables", 15.0, 0.7, 3.6, 0.1, 150.0, "1/2 large cucumber (150g)"),
        FoodItem("v_bell_pepper", "Sweet Bell Pepper", "Vegetables", 31.0, 1.0, 6.0, 0.3, 120.0, "1 whole pepper (120g)"),
        FoodItem("v_sweet_potato", "Baked Sweet Potato", "Vegetables", 90.0, 2.0, 20.7, 0.2, 150.0, "1 medium potato (150g)"),
        FoodItem("v_potato", "Boiled Potato", "Vegetables", 87.0, 1.9, 20.1, 0.1, 180.0, "1 medium potato (180g)"),
        FoodItem("v_onion", "Yellow Onion", "Vegetables", 40.0, 1.1, 9.3, 0.1, 110.0, "1 medium onion (110g)"),
        FoodItem("v_mushrooms", "White Button Mushrooms", "Vegetables", 22.0, 3.1, 3.3, 0.3, 100.0, "1 cup sliced (100g)"),

        // Grains & Bakery
        FoodItem("g_oats", "Rolled Oats (Dry)", "Grains & Bakery", 389.0, 16.9, 66.3, 6.9, 50.0, "1/2 cup dry oats (50g)"),
        FoodItem("g_white_rice", "Cooked White Rice", "Grains & Bakery", 130.0, 2.7, 28.2, 0.3, 160.0, "1 cup cooked (160g)"),
        FoodItem("g_brown_rice", "Cooked Brown Rice", "Grains & Bakery", 112.0, 2.3, 23.5, 0.8, 160.0, "1 cup cooked (160g)"),
        FoodItem("g_whole_bread", "Whole Wheat Bread", "Grains & Bakery", 247.0, 13.0, 41.0, 3.4, 40.0, "1 slice (40g)"),
        FoodItem("g_white_bread", "White Toast Bread", "Grains & Bakery", 265.0, 9.0, 49.0, 3.2, 35.0, "1 slice (35g)"),
        FoodItem("g_quinoa", "Cooked Quinoa", "Grains & Bakery", 120.0, 4.4, 21.3, 1.9, 185.0, "1 cup cooked (185g)"),
        FoodItem("g_pasta", "Cooked Pasta (Spaghetti)", "Grains & Bakery", 158.0, 5.8, 30.9, 0.9, 150.0, "1 cup cooked (150g)"),
        FoodItem("g_bagel", "Plain Bagel", "Grains & Bakery", 250.0, 10.0, 50.0, 1.5, 100.0, "1 regular bagel (100g)"),
        FoodItem("g_corn_tortilla", "Corn Tortilla", "Grains & Bakery", 218.0, 5.7, 44.6, 2.8, 30.0, "1 tortilla (30g)"),

        // Protein & Meat
        FoodItem("p_chicken_breast", "Grilled Chicken Breast", "Protein & Meat", 165.0, 31.0, 0.0, 3.6, 150.0, "1 breast fillet (150g)"),
        FoodItem("p_salmon", "Baked Atlantic Salmon", "Protein & Meat", 208.0, 20.0, 0.0, 13.0, 170.0, "1 salmon fillet (170g)"),
        FoodItem("p_egg", "Boiled Egg (Large)", "Protein & Meat", 155.0, 12.6, 1.1, 10.6, 50.0, "1 large egg (50g)"),
        FoodItem("p_egg_whites", "Liquid Egg Whites", "Protein & Meat", 52.0, 11.0, 0.7, 0.2, 100.0, "100ml egg whites"),
        FoodItem("p_tuna", "Canned Tuna in Water", "Protein & Meat", 116.0, 26.0, 0.0, 1.0, 120.0, "1 drained can (120g)"),
        FoodItem("p_beef_ground", "Lean Ground Beef (90%)", "Protein & Meat", 176.0, 26.0, 0.0, 8.0, 150.0, "1 cooked patty (150g)"),
        FoodItem("p_tofu", "Firm Tofu", "Protein & Meat", 83.0, 10.0, 1.9, 5.3, 125.0, "1/2 block (125g)"),
        FoodItem("p_whey", "Whey Protein Powder", "Protein & Meat", 400.0, 80.0, 8.0, 4.0, 32.0, "1 rounded scoop (32g)"),
        FoodItem("p_lentils", "Cooked Brown Lentils", "Protein & Meat", 116.0, 9.0, 20.0, 0.4, 200.0, "1 cup cooked (200g)"),
        FoodItem("p_black_beans", "Cooked Black Beans", "Protein & Meat", 132.0, 8.9, 23.7, 0.5, 170.0, "1 cup cooked (170g)"),
        FoodItem("p_shrimp", "Cooked Shrimp", "Protein & Meat", 99.0, 24.0, 0.2, 0.3, 100.0, "10-12 medium shrimp (100g)"),

        // Dairy & Eggs
        FoodItem("d_greek_yogurt", "Greek Yogurt (Non-fat)", "Dairy & Eggs", 59.0, 10.0, 3.6, 0.4, 170.0, "1 container (170g)"),
        FoodItem("d_whole_milk", "Whole Milk (3.25%)", "Dairy & Eggs", 61.0, 3.2, 4.8, 3.3, 240.0, "1 cup glass (240ml)"),
        FoodItem("d_skim_milk", "Skim Milk (0%)", "Dairy & Eggs", 35.0, 3.4, 5.0, 0.1, 240.0, "1 cup glass (240ml)"),
        FoodItem("d_almond_milk", "Unsweetened Almond Milk", "Dairy & Eggs", 15.0, 0.5, 0.3, 1.1, 240.0, "1 cup glass (240ml)"),
        FoodItem("d_cheddar", "Cheddar Cheese", "Dairy & Eggs", 403.0, 25.0, 1.3, 33.0, 30.0, "1 slice/portion (30g)"),
        FoodItem("d_mozzarella", "Fresh Mozzarella", "Dairy & Eggs", 280.0, 28.0, 3.1, 17.0, 45.0, "1 ball/slice (45g)"),
        FoodItem("d_cottage_cheese", "Cottage Cheese (2%)", "Dairy & Eggs", 86.0, 11.8, 3.7, 2.3, 150.0, "1/2 cup (150g)"),
        FoodItem("d_butter", "Salted Butter", "Dairy & Eggs", 717.0, 0.9, 0.1, 81.0, 14.0, "1 tablespoon (14g)"),

        // Nuts & Healthy Fats
        FoodItem("n_almonds", "Raw Almonds", "Nuts & Healthy Fats", 579.0, 21.0, 22.0, 50.0, 30.0, "1 handful (30g)"),
        FoodItem("n_peanut_butter", "Natural Peanut Butter", "Nuts & Healthy Fats", 588.0, 25.0, 20.0, 50.0, 32.0, "2 tablespoons (32g)"),
        FoodItem("n_olive_oil", "Extra Virgin Olive Oil", "Nuts & Healthy Fats", 884.0, 0.0, 0.0, 100.0, 14.0, "1 tablespoon (14g)"),
        FoodItem("n_walnuts", "Walnuts", "Nuts & Healthy Fats", 654.0, 15.2, 13.7, 65.2, 30.0, "1 handful (30g)"),
        FoodItem("n_chia", "Chia Seeds", "Nuts & Healthy Fats", 486.0, 16.5, 42.1, 30.7, 15.0, "1 tablespoon (15g)"),

        // Meals & Snacks
        FoodItem("m_granola_bar", "Oat & Honey Granola Bar", "Meals & Snacks", 410.0, 7.0, 70.0, 12.0, 40.0, "1 bar (40g)"),
        FoodItem("m_dark_chocolate", "Dark Chocolate (70%)", "Meals & Snacks", 598.0, 7.8, 46.0, 43.0, 30.0, "3 squares (30g)"),
        FoodItem("m_pizza", "Pepperoni Pizza Slice", "Meals & Snacks", 266.0, 11.0, 30.0, 11.0, 107.0, "1 medium slice (107g)"),
        FoodItem("m_burger", "Classic Cheeseburger", "Meals & Snacks", 250.0, 13.0, 24.0, 11.0, 160.0, "1 burger (160g)"),
        FoodItem("m_caesar_salad", "Chicken Caesar Salad", "Meals & Snacks", 125.0, 9.5, 4.5, 7.8, 250.0, "1 full bowl (250g)"),
        FoodItem("m_hummus", "Classic Hummus Dip", "Meals & Snacks", 166.0, 7.9, 14.3, 9.6, 60.0, "4 tablespoons (60g)"),
        FoodItem("m_pita", "Whole Wheat Pita Bread", "Meals & Snacks", 262.0, 9.8, 55.7, 1.7, 60.0, "1 pita round (60g)"),
        FoodItem("m_coffee_latte", "Cafe Latte (with Milk)", "Meals & Snacks", 54.0, 3.2, 5.0, 2.3, 300.0, "1 medium mug (300ml)")
    )
}
