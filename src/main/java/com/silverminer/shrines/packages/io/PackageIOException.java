package com.silverminer.shrines.packages.io;

import com.silverminer.shrines.utils.CalculationError;

public class PackageIOException extends Exception {
   private final CalculationError reason;

   public PackageIOException(CalculationError reason) {
      super(reason.toString());
      this.reason = reason;
   }


   public CalculationError getReason() {
      return reason;
   }
}
