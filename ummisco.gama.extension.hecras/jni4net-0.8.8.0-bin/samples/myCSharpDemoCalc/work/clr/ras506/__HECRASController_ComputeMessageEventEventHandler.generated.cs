//------------------------------------------------------------------------------
// <auto-generated>
//     This code was generated by jni4net. See http://jni4net.sourceforge.net/ 
//     Runtime Version:4.0.30319.42000
//
//     Changes to this file may cause incorrect behavior and will be lost if
//     the code is regenerated.
// </auto-generated>
//------------------------------------------------------------------------------

namespace RAS506 {
    
    
    #region Component Designer generated code 
    public partial class @__HECRASController_ComputeMessageEventEventHandler_ {
        
        public static global::java.lang.Class _class {
            get {
                return global::RAS506.____HECRASController_ComputeMessageEventEventHandler.staticClass;
            }
        }
    }
    #endregion
    
    #region Component Designer generated code 
    [global::net.sf.jni4net.attributes.JavaProxyAttribute(typeof(global::RAS506.@__HECRASController_ComputeMessageEventEventHandler), typeof(global::RAS506.@__HECRASController_ComputeMessageEventEventHandler_))]
    [global::net.sf.jni4net.attributes.ClrWrapperAttribute(typeof(global::RAS506.@__HECRASController_ComputeMessageEventEventHandler), typeof(global::RAS506.@__HECRASController_ComputeMessageEventEventHandler_))]
    internal sealed partial class ____HECRASController_ComputeMessageEventEventHandler : global::java.lang.Object {
        
        internal new static global::java.lang.Class staticClass;
        
        internal static global::net.sf.jni4net.jni.MethodId j4n_Invoke0;
        
        private ____HECRASController_ComputeMessageEventEventHandler(global::net.sf.jni4net.jni.JNIEnv @__env) : 
                base(@__env) {
        }
        
        private static void InitJNI(global::net.sf.jni4net.jni.JNIEnv @__env, java.lang.Class @__class) {
            global::RAS506.____HECRASController_ComputeMessageEventEventHandler.staticClass = @__class;
            global::RAS506.____HECRASController_ComputeMessageEventEventHandler.j4n_Invoke0 = @__env.GetMethodID(global::RAS506.____HECRASController_ComputeMessageEventEventHandler.staticClass, "Invoke", "(Ljava/lang/String;)V");
        }
        
        public void Invoke(string eventMessage) {
            global::net.sf.jni4net.jni.JNIEnv @__env = this.Env;
            using(new global::net.sf.jni4net.jni.LocalFrame(@__env, 12)){
            @__env.CallVoidMethod(this, global::RAS506.____HECRASController_ComputeMessageEventEventHandler.j4n_Invoke0, global::net.sf.jni4net.utils.Convertor.ParStrongC2JString(@__env, eventMessage));
            }
        }
        
        private static global::System.Collections.Generic.List<global::net.sf.jni4net.jni.JNINativeMethod> @__Init(global::net.sf.jni4net.jni.JNIEnv @__env, global::java.lang.Class @__class) {
            global::System.Type @__type = typeof(____HECRASController_ComputeMessageEventEventHandler);
            global::System.Collections.Generic.List<global::net.sf.jni4net.jni.JNINativeMethod> methods = new global::System.Collections.Generic.List<global::net.sf.jni4net.jni.JNINativeMethod>();
            methods.Add(global::net.sf.jni4net.jni.JNINativeMethod.Create(@__type, "Invoke", "Invoke0", "(Ljava/lang/String;)V"));
            return methods;
        }
        
        private static void Invoke0(global::System.IntPtr @__envp, global::net.sf.jni4net.utils.JniLocalHandle @__obj, global::net.sf.jni4net.utils.JniLocalHandle eventMessage) {
            // (Ljava/lang/String;)V
            // (LSystem/String;)V
            global::net.sf.jni4net.jni.JNIEnv @__env = global::net.sf.jni4net.jni.JNIEnv.Wrap(@__envp);
            try {
            global::RAS506.@__HECRASController_ComputeMessageEventEventHandler @__real = global::net.sf.jni4net.utils.Convertor.StrongJ2CpDelegate<global::RAS506.@__HECRASController_ComputeMessageEventEventHandler>(@__env, @__obj);
            @__real.Invoke(global::net.sf.jni4net.utils.Convertor.StrongJ2CString(@__env, eventMessage));
            }catch (global::System.Exception __ex){@__env.ThrowExisting(__ex);}
        }
        
        new internal sealed class ContructionHelper : global::net.sf.jni4net.utils.IConstructionHelper {
            
            public global::net.sf.jni4net.jni.IJvmProxy CreateProxy(global::net.sf.jni4net.jni.JNIEnv @__env) {
                return new global::RAS506.____HECRASController_ComputeMessageEventEventHandler(@__env);
            }
        }
    }
    #endregion
}
