package dalvik.system;
import java.nio.ByteBuffer;
import dalvik.system.Taint;

public class Declassifier{

/**
 * @hide
 */
  public static void RemoveTaint(String src)
  {
	Taint.RemoveTaintString(src);
  }
 
/**
 * @hide
 */ 
  public static void RemoveTaint(Double src)
  {
	Taint.RemoveTaintDouble(src);
  }
  
}
		

