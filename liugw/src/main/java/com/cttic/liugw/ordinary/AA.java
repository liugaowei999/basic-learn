package com.cttic.liugw.ordinary;

import java.io.Serializable;

public class AA
implements Serializable
{
int a;
transient String bString;

public AA() {
    
}

public AA(int a, String bString)
{
  this.a = a;
  this.bString = bString;
}

//public String toString()
//{
//  return "a = " + this.a + ", bString = " + this.bString;
//}
}