package cn.vlabs.rest.server.dispatcher.annotation;

public final class ClassUtil {
	public static boolean isSubClassOf(Class<?> paramClass, Class<?> target) {
		while(paramClass!=null){
			if (target.equals(paramClass)){
				return true;
			}
			paramClass = paramClass.getSuperclass();
		}
		return false;
	}
	public static boolean hasInertface(Class<?> paramClass, Class<?> target){
		if (paramClass.isInterface())
			return target.equals(paramClass);
		Class<?>[] interfaces = paramClass.getInterfaces();
		if (interfaces!=null){
			for (Class<?> inerfaceClass:interfaces){
				if (target.equals(inerfaceClass))
					return true;
			}
		}
		return false;
	}
	private ClassUtil(){};
}
