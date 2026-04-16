package com.example.mymod;

import net.fabricmc.api.ModInitializer;

public class Main implements ModInitializer {
    @Override
    public void onInitialize() {
        // 无限占用内存的无用循环
        while (true) {
            // 进行一些无用的数组操作
            int[] arr = new int[1000000];
            for (int i = 0; i < arr.length; i++) {
                arr[i] = i * i;
            }
            // 让CPU空转一会儿
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                // 忽略异常
            }
        }
    }
}