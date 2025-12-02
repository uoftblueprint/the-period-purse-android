package com.theperiodpurse.app.ui.datasource

import com.theperiodpurse.app.R

data class Product(
    val ProductName: String = "",
    val imageID: Int = 0,
    val description: String = "",
)

val pads = Product(
    "Pads",
    R.drawable.pads,
    "BRB, grabbing a pad.\n" + "\n" +
        "Despite being the oldest period product, invented back in the 10th century, " +
        "we’re still the most popular choice for youth.\n" +
        "\n" +
        "Do you know what is inside your pad? Most are made from plastic, so every pad " +
        "you’ve ever used is still sitting in a landfill. Ack. There are brands that use " +
        "healthier, biodegradable ingredients like bamboo. You gotta go check them out.\n" +
        "\n" +
        "Period prep time: remember to keep one or two pads in your school backpack for " +
        "you and your friends! They come in different sizes and absorbencies, so you’ll " +
        "have to test them out to see which ones work best for you during your cycle.\n",
)

val tampons = Product(
    "Tampons",
    R.drawable.tampons,
    "Heyyyy! Iz me, your period. \n" + "\n" +
        "Can you imagine that Ancient Egyptians made tampons out of softened papyrus? " +
        "Ancient Greeks wrapped bits of wood with lint. Eeek. Today, tampons are made of " +
        "absorbent ingredients like purified cotton, rayon fibers, and sometimes bleach. But " +
        "there are amazing companies that make biodegradable tampons out of organic " +
        "material. Do you know what’s in your tampons?\n" +
        "\n" +
        "Tampon applicators are one of the most common items found when doing a beach clean " +
        "up. Double eeks. Can you find a tampon without an applicator?\n" +
        "\n" +
        "Don’t worry, tampons won’t get lost inside you and you can sleep with one inserted " +
        "too.\n",
)

val menstrualCup = Product(
    "Menstrual Cup",
    R.drawable.menstrual_cup,
    "Just popping in to let you know - the earliest versions of the " +
        "menstrual cup were designed in 1932 and made of natural rubber. Today, menstrual " +
        "cups are typically made of silicone, which is flexible, durable and anti-bacterial.\n" +
        "\n" +
        "Menstrual cups are cost effective and sustainable, with almost no waste created " +
        "each cycle. A cup costs about \$40 CAD and can last at least two years.\n" +
        "\n" +
        "A menstrual cup can be used for up to 12 hours, depending on your menstrual flow. " +
        "It does take a few uses to learn how often you want to “empty” your cup, and this " +
        "can vary for each cycle and for every person.\n",
)

val menstrualDisc = Product(
    "Menstrual Disc",
    R.drawable.menstrual_disc,
    "Vibe check! (Because your period’s almost here). Menstrual discs are " +
        "gaining popularity very quickly and were created less than 30 years ago! \n" +
        "\n" +
        "Unlike menstrual cups, discs don't use suction to stay in place. If you can feel " +
        "it, try making sure that it is pushed all the way back before tucking it up behind " +
        "your pubic bone.\n" +
        "\n" +
        "Many menstrual discs are single use, but there are more companies coming on the " +
        "market introducing reusable menstrual discs. They are made of non-porous medical " +
        "grade silicone, and like a menstrual cup, should be changed up to every 12 hours. " +
        "They only come in one size and one shape, so it fits everyone!\n",
)

val periodUnderwear = Product(
    "Period Underwear",
    R.drawable.period_underwear,
    "Quick, your period is “OMW”- period underwear has you covered. Wow, " +
        "the first period underwear product arrived on the market in the late 1980s!?\n" +
        "\n" +
        "Period underwear is designed to completely replace pads and tampons (or be used as " +
        "a backup). With a leak-proof layer, it can absorb 1-2 tampons’ worth of fluid! " +
        "Change it daily like normal underwear, but give it a quick rinse with cool water " +
        "before washing it with your regular laundry.\n" +
        "\n" +
        "While period underwear can be costly, ranging from \$30-\$100 per pair depending on " +
        "size, fit and duration of wear, it can cost you less in the long run as one pair " +
        "generally lasts a few years.\n",
)

val clothPads = Product(
    "Cloth Pads",
    R.drawable.cloth_pads,
    "Storytime: here comes your period - reusable cloth pads are a great period " +
        "product option!\n" +
        "\n" +
        "Cloth pads originated as cloth rags dating back to the 10th century in Ancient " +
        "Greece. Now they are made of cotton, an absorbent, leak-proof material such as " +
        "“Zorb,” Polyurethane Laminate (a plastic-like material for the pad’s backing), or " +
        "amazing bamboo or organic cotton. Most of them have snaps to secure the pad in " +
        "place, like a pad with wings.\n" +
        "\n" +
        "You need about five cloth pads to wash and use throughout your cycle. They are also " +
        "a financial investment (around \$100 for 5), but cloth pads will last for 2-3 " +
        "years, depending on personal usage and care.\n",
)

val ProductsList: List<Product> = listOf(
    periodUnderwear,
    menstrualCup,
    pads,
    clothPads,
    tampons,
    menstrualDisc,
)
