/*
 * Copyright (c) 2022.
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.silverminer.shrines.gui.packets.edit.structures;

import com.silverminer.shrines.gui.misc.IDoubleClickScreen;
import com.silverminer.shrines.gui.packets.edit.pools.EditPoolsList;
import com.silverminer.shrines.packages.datacontainer.StructuresPackageWrapper;
import com.silverminer.shrines.packages.datacontainer.TemplatePool;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.function.Supplier;

/**
 * @author Silverminer
 */
@OnlyIn(Dist.CLIENT)
public class SelectPoolList extends EditPoolsList {
   protected static final Logger LOGGER = LogManager.getLogger(SelectPoolList.class);
   protected final IDoubleClickScreen doubleClickScreen;
   protected ResourceLocation selectedPool;

   public SelectPoolList(Minecraft mc, int p_i49846_3_, int p_i49846_4_, int p_i49846_5_, int p_i49846_6_, int p_i49846_7_, StructuresPackageWrapper packet, IDoubleClickScreen screen, ResourceLocation selectedPool) {
      super(mc, p_i49846_3_, p_i49846_4_, p_i49846_5_, p_i49846_6_, p_i49846_7_, packet, screen);
      this.doubleClickScreen = screen;
      this.selectedPool = selectedPool;
   }

   @Override
   public void refreshList(Supplier<String> search) {
      if (this.selectedPool == null) {
         return;
      }
      this.clearEntries();

      List<TemplatePool> pools = packet.getPools().getAsList();
      Collections.sort(pools);
      String s = search.get().toLowerCase(Locale.ROOT);

      for (TemplatePool pool : pools) {
         if (pool.getName().toString().toLowerCase(Locale.ROOT).contains(s)) {
            SelectPoolList.SelectEntry entry = new SelectPoolList.SelectEntry(pool);
            this.addEntry(entry);
            if (Objects.equals(pool.getName(), this.selectedPool)) {
               this.setSelected(entry);
            }
         }
      }
   }

   @Override
   public void setSelected(@Nullable EditPoolsList.Entry entry) {
      if (entry != null && entry.getPool() != null) {
         this.selectedPool = entry.getPool().getName();
      } else {
         this.selectedPool = null;
      }
      super.setSelected(entry);
   }

   @OnlyIn(Dist.CLIENT)
   public final class SelectEntry extends EditPoolsList.Entry {

      public SelectEntry(TemplatePool pool) {
         super(pool);
      }

      public boolean mouseClicked(double mouseX, double mouseY, int scrolledAmount) {
         SelectPoolList.this.setSelected(this);
         if (Util.getMillis() - this.lastClickTime < 250L) {
            this.configure();
            return true;
         } else {
            this.lastClickTime = Util.getMillis();
            return false;
         }
      }

      public void configure() {
         SelectPoolList.this.doubleClickScreen.onEntryDoubleClicked();
      }
   }
}