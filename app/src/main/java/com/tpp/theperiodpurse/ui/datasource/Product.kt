package com.tpp.theperiodpurse.ui.datasource

import com.tpp.theperiodpurse.R

sealed class ContentBlock
data class TextBlock(val text: String) : ContentBlock()
data class ImageBlock(val imageResId: Int) : ContentBlock() // Commented out for now

data class Product(
    val productName: String = "",
    val imageID: Int = 0,
    val descriptionBlocks: List<ContentBlock> = emptyList()
)

val pads = Product(
    "Pads",
    R.drawable.pads,
    listOf(
        ImageBlock(R.drawable.padstep1),
        TextBlock("Put in underwear and snap into place."),

        ImageBlock(R.drawable.padstep2),
        TextBlock("Put in underwear."),

        ImageBlock(R.drawable.padstep3),
        TextBlock("Wear for 3-5 hours."),

        ImageBlock(R.drawable.padstep4),
        TextBlock("Discard pad in trash.")
    )
)

val tampons = Product(
    "Tampons",
    R.drawable.tampons,
    listOf(
        // ImageBlock(R.drawable.tampon_ancient),
        TextBlock("Ancient Egyptians made tampons out of softened papyrus."),

        // ImageBlock(R.drawable.tampon_modern),
        TextBlock("Today, tampons are made of absorbent materials like cotton and rayon."),

        // ImageBlock(R.drawable.tampon_environment),
        TextBlock("Tampon applicators are commonly found in beach cleanups—can you find tampons without applicators?")
    )
)

val menstrualCup = Product(
    "Menstrual Cup",
    R.drawable.menstrual_cup,
    listOf(
        ImageBlock(R.drawable.mcstep1),
        TextBlock("Fold in half"),
        ImageBlock(R.drawable.mcstep2),
        TextBlock("Insert with your fingers"),
        ImageBlock(R.drawable.mcstep3),
        TextBlock("Wear up to 12 hours"),
        ImageBlock(R.drawable.mcstep4),
        TextBlock("Pinch cup to release section"),
        ImageBlock(R.drawable.mcstep5),
        TextBlock("Empty cup"),
        ImageBlock(R.drawable.mcstep6),
        TextBlock("Wash cup")
    )
)

val menstrualDisc = Product(
    "Menstrual Disc",
    R.drawable.menstrual_disc,
    listOf(
        ImageBlock(R.drawable.mdstep1),
        TextBlock("Fold in half"),

        ImageBlock(R.drawable.mdstep2),
        TextBlock("Insert with your fingers"),

        ImageBlock(R.drawable.mdstep3),
        TextBlock("Wear up to 12 hours"),

        ImageBlock(R.drawable.mdstep4),
        TextBlock("Hook rim with fingers to remove"),

        ImageBlock(R.drawable.mdstep5),
        TextBlock("Empty disc"),

        ImageBlock(R.drawable.mdstep6),
        TextBlock("Wash disc"),

    )
)

val periodUnderwear = Product(
    "Period Underwear",
    R.drawable.period_underwear,
    listOf(
         ImageBlock(R.drawable.pustep1),
        TextBlock("Put on underwear"),

         ImageBlock(R.drawable.pustep2),
        TextBlock("Wear up to 12 hours"),

        ImageBlock(R.drawable.pustep3),
        TextBlock("Hand wash OR Machine wash"),

         ImageBlock(R.drawable.pustep5),
        TextBlock("Hang to dry")
    )
)

val clothPads = Product(
    "Cloth Pads",
    R.drawable.cloth_pads,
    listOf(
         ImageBlock(R.drawable.clothpadstep1),
        TextBlock("Put in underwear and snap into place"),

         ImageBlock(R.drawable.clothpadstep2),
        TextBlock("Wear for 3-5 hours"),

         ImageBlock(R.drawable.clothpadstep3),
        TextBlock("Hand wash OR Machine wash"),

        ImageBlock(R.drawable.clothpadstep4),
        TextBlock("Hang to dry")
    )
)

val ProductsList: List<Product> = listOf(
    periodUnderwear,
    menstrualCup,
    pads,
    clothPads,
    tampons,
    menstrualDisc
)
