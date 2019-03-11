package org.spookit.api.userinterface;

public class Variable<T> {

	public Variable(T t) {
		var = t;
	}

	public Variable() {
	}

	T var;

	public T get() {
		return var;
	}

	public void set(T t) {
		var = t;
	}

	public String toString() {
		return var.toString();
	}

	public void finalize() {
		System.out.println("Wow");
	}
	
	public static void main(String[]args) {
		new Variable<>("");
		for (int i =0;i<100;i++)System.gc();
	}
	
}
