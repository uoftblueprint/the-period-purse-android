package com.tpp.theperiodpurse.ui.theme

import androidx.compose.ui.graphics.Color
import com.tpp.theperiodpurse.R
import com.tpp.theperiodpurse.ui.education.pink


interface ColorPalette {
    val primary1: Color
    val primary2: Color
    val primary3: Color
    val secondary1: Color
    val secondary2: Color
    val secondary3: Color
    val secondary4: Color
    val text1: Color
    val text2: Color
    val lineColor: Color

    val HeaderColor1: Color
    val BottomBarColor1: Color
    val SelectedColor1: Color
    val LogSelectedTextColor: Color
    val CalendarDayColor: Color
    val CalendarFontColor: Color
    val PopUpHeadColor: Color
    val MainFontColor: Color
    val SecondaryFontColor: Color
    val ButtonDisabledColor: Color
    val cyclePink: Color
    val cycleBlue: Color
    val popUpTopBar: Color
    val productBackground: Color

    val background: Int
}

class DarkColorPaletteImpl : ColorPalette {
    override val primary1 = Color(0xFF72C6B7)
    override val primary2 = Color(	0xFFD32729)
    override val primary3 = Color(0xFF7DA198)
    override val secondary1 = Color(0xFF5A9F93)
    override val secondary2 = Color(0xFF73c7b7)
    override val secondary3 = Color(0xFFFA6767)
    override val secondary4 = Color(0xFF6D6E71)
    override val text1 = Color(	217, 218, 217)
    override val text2 = Color(161, 162, 166)
    override val lineColor = Color(0xFFFFFFFF)

    override val HeaderColor1 = Color(0xFF090909)
    override val BottomBarColor1 = Color(0xFF090909)
    override val SelectedColor1 = Color(0xFF589E92)
    override val LogSelectedTextColor = Color(0xFF6D6E71)
    override val PopUpHeadColor = Color(	161, 162, 166)
    override val CalendarDayColor = Color(0xFF6D6E71)
    override val CalendarFontColor = Color(0xFF6D6E71)
    override val MainFontColor = Color(0xFFF1F1F1)
    override val SecondaryFontColor = Color(161, 162, 166)
    override val ButtonDisabledColor = Color(0xFFA9BDBA)
    override val cyclePink = Color(	250, 103, 103)
    override val cycleBlue = Color(	90, 159, 147)
    override val popUpTopBar = Color(0xFF282D2D)
    override val productBackground = Color(0xFF6D6E71).copy(0.6f)


    override val background: Int= R.drawable.darkwatercolour

    // Implement other color properties for dark mode
}

class LightColorPaletteImpl : ColorPalette {
    override val primary1 = Color(0xFF72C6B7)
    override val primary2 = Color(	0xFFF02E30)
    override val primary3 = Color(0xFF7DA198)
    override val secondary1 = Color(0xFF5A9F93)
    override val secondary2 = Color(0xFF73c7b7)
    override val secondary3 = Color(0xFFB31F20)
    override val secondary4 = Color(0xFFEFEFF4)
    override val text1 = Color.DarkGray
    override val text2 = Color.Gray
    override val lineColor = Color(0xFFCFCFCF)

    override val HeaderColor1 = Color.White
    override val BottomBarColor1 = Color(0xFFFFFFFF)
    override val SelectedColor1 = Color(0xFF589E92)
    override val LogSelectedTextColor = Color(0xFF6D6E71)
    override val PopUpHeadColor =  Color.Black
    override val CalendarDayColor = Color.White
    override val CalendarFontColor = Color(0xFF090909)
    override val MainFontColor = Color(0xFF090909)
    override val SecondaryFontColor = Color(0xFF717173)
    override val ButtonDisabledColor = Color(0xFFA9BDBA)
    override val cyclePink = Color(0xFFFEDBDB)
    override val cycleBlue = Color(0xFFBAE0D8)
    override val popUpTopBar = Color(0xFFEFEFF4)
    override val productBackground = Color(pink)

    override val background: Int = R.drawable.background
}

