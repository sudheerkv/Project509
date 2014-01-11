*Please copy the attached files and paste them in the Android parent (base) directory.
After copying, assuming TaintDroid is integrated within the Android souce, follow the below commands to execute the code.

$mmm dalvik/
$mmm frameworks/base/
$mmm libcore/
$mmm packages/apps/TaintDroidNotify/

-- Following is the file where the un-tainting of the data is done.
/dalvik/vm/native/dalvik_system_Taint.cpp
-- Three methods are added:
static void Dalvik_dalvik_system_Taint_RemoveTaintString(const u4* args, JValue* pResult)
static void Dalvik_dalvik_system_Taint_RemoveTaintDouble(const u4* args, JValue* pResult)
static void Dalvik_dalvik_system_Taint_TaintException(const u4* args, JValue* pResult)

-- The following are the sink points where the exceptions are added.
libcore/luni/src/main/java/libcore/io/Posix.java
libcore/luni/src/main/java/org/apache/harmony/xnet/provider/jsse/OpenSSLSocketImpl.java
frameworks/base/telephony/java/com/android/internal/telephony/gsm/GSMSMSDispatcher.java


-- The following are the 2 extra classes added in the libcore package 
libcore/dalvik/src/main/java/dalvik/system/ReferenceMonitor.java
libcore/dalvik/src/main/java/dalvik/system/Declassifier.java

-- Native methods of RemoveTaintString, RemoveTaintDouble and TaintException are added in Taint.java
libcore/dalvik/src/main/java/dalvik/system/Taint.java

-- The following is the file in which App specific details are implemented
packages/apps/TaintDroidNotify/src/org/appanalysis/TaintDroidNotifyController.java

-- Changes related to resources are made in these files 
packages/apps/TaintDroidNotify/AndroidManifest.xml
/packages/apps/TaintDroidNotify/res/layout/control.xml
/packages/apps/TaintDroidNotify/res/layout/strings.xml