# SwitchButtonIphoneStyle
SwitchButtonIphoneStyle

代码用于仿iphone 开关。
大体的思路 就是重写view,然后画椭圆背景，画圆圈儿，
然后根据touch事件来处理滑动的方向 滑动的距离，
通过方向和距离 重新画背景和圆圈的位置。
并且增加了statusChangedListener的接口 ，用于callback。
可以通过attr来修改对应的背景色和圆圈的颜色

这里设置了最大宽度和最小宽度，所以在布局文件中，
设置的宽度大于最大宽度或者小于最小宽度，会被代码默认为最大或者最小宽度
高度为当前宽度/2.所以在布局文件中设置高度也不会起作用。
这样是为了保证宽度和高度的比例一直，防止界面显示问题

