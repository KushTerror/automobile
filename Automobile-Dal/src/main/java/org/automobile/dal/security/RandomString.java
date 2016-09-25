/*
 * Copyright (c) 2015 FC-POS Compulynx LTD.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are NOT permitted.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL COMPULYNX LTD BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.automobile.dal.security;

import java.security.SecureRandom;

/**
 * @author Kushal
 */
public class RandomString {

   private static final char[] availChars;

   static {
      StringBuilder stringBuilder = new StringBuilder();
      for (char ch = '0'; ch <= '9'; ++ch) {
         stringBuilder.append(ch);
      }
      for (char ch = 'a'; ch <= 'z'; ++ch) {
         stringBuilder.append(ch);
      }
      availChars = stringBuilder.toString().toCharArray();
   }

   private final SecureRandom random = new SecureRandom();

   private final char[] generatedChars;

   public RandomString(int length) {
      if (length < 1) {
         throw new IllegalArgumentException("length < 1: " + length);
      }
      generatedChars = new char[length];
   }

   public String nextString() {
      for (int idx = 0; idx < generatedChars.length; ++idx) {
         generatedChars[idx] = availChars[random.nextInt(availChars.length)];
      }
      return new String(generatedChars);
   }
}
