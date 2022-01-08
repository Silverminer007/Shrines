/*
 * Copyright (c) 2022.
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.silverminer.shrines.utils.client;

import java.awt.Color;

/**
 * @author Silverminer
 */
public class ColorLoop {
   private int red = 0;
   private int green = 0;
   private int blue = 0;

   public ColorLoop() {
   }

   public int getRed() {
      return this.red;
   }

   public int getGreen() {
      return this.green;
   }

   public int getBlue() {
      return this.blue;
   }

   public int getRGB() {
      return this.getColor().getRGB();
   }

   public Color getColor() {
      return new Color(red % 256, green % 256, blue % 256);
   }

   public void tick() {
      int speed = 3;
      if (blue < 255 && red <= 0 && green <= 0) {
         blue += speed;
         return;
      }
      if (red < 255 && blue >= 255 && green <= 0) {
         red += speed;
         return;
      }
      if (blue > 0 && red >= 255 && green <= 0) {
         blue -= speed;
         return;
      }
      if (green < 255 && blue <= 0 && red >= 255) {
         green += speed;
         return;
      }
      if (red > 0 && blue <= 0 && green >= 255) {
         red -= speed;
         return;
      }
      if (blue < 255 && red <= 0 && green >= 255) {
         blue += speed;
         return;
      }
      if (green > 0 && red <= 0 && blue >= 255) {
         green -= speed;
         return;
      }
   }
}