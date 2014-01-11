package dalvik.system;
import java.nio.ByteBuffer;

public class ReferenceMonitor {
 /**
 * @hide
 */
   public static void RemoveTaint(String str)
   {
	Declassifier.RemoveTaint(str);
   
   }
   
/**
 * @hide
 */
      public static void RemoveTaint(Double d)
   {
	 Declassifier.RemoveTaint(d);
   }

 }
	
