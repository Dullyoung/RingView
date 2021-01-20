# RingView
# 使用方法:
 

```java
RingView ringView = new RingView(this, 400);
        ringView.setData(new int[]{0xffff9b27, 0xffff0025, 0xff000000, 0xff663000},
                new float[]{0.2f, 0.2f, 0.3f, 0.3f}, 40);
        mLlRoot.addView(ringView);
        ringView.setDurationAndStartAnim(6000);
```

​        

# 可以直接把RingView这个类copy到你自己的项目即可

## 暂时没有做xml兼容 仅支持代码addview