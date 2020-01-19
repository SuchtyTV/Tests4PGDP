package pgdp.threads;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.function.BinaryOperator;
import java.util.function.Function;

import org.junit.jupiter.api.Test;

public class Tests {
	
	@Test
	public void testFactorial(){
		/*
		 * Test with more threads than n
		 * Assuming your facSequential is implemented correctly...
		 */

	
		BigInteger expected = Factorial.facSequential(100);
		try {
			Factorial.facParallel(100, 101); // is this reacting correctly?
			BigInteger actual = Factorial.facParallel(100, 12);
			assertEquals(actual, expected);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			
		}
		
	}
	
	//@Test
	public void testFactorialPerformance() {
		long before = System.currentTimeMillis();
		try {
			BigInteger actual = Factorial.facParallel(1000000, 8);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		long end = System.currentTimeMillis();
		if(Runtime.getRuntime().availableProcessors() >= 2) {
			assertTrue(end - before <= 6000000, "Slow multicore performance");
		} else {
			assertTrue(end - before <= 12000000, "Slow multicore performance");
		}
		
	}
	
	@Test
	public void reduceTest() {
		BinaryOperator<BigInteger> a = BigInteger::multiply;
		BigInteger[] data = new BigInteger[100];
		for(int i = 0; i< 100; i++) {
			data[i] = BigInteger.valueOf(i+1);
		}
		BigInteger expected = new BigInteger("93326215443944152681699238856266700490715968264381621468592963895217599993229915608941463976156518286253697920827223758251185210916864000000000000000000000000");
		try {
			BigInteger actual = ParallelCompute.parallelReduceArray(data, a, 4);
			ParallelCompute.parallelReduceArray(data, a, 0);
			assertEquals(expected, actual);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch(IllegalArgumentException e) {
			System.out.println("Should have happend");
		}
		
		try {
			ParallelCompute.parallelReduceArray(null, a, 4);
		} catch (NullPointerException e) {
			System.out.println("Should have happend");
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		data = new BigInteger[3];
		for(int i = 0; i< 3; i++) {
			data[i] = BigInteger.valueOf(i+1);
		}
		
		a = BigInteger::add;
		try {
			assertEquals(ParallelCompute.parallelReduceArray(data, a, 2), BigInteger.valueOf(6));
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
		    assertEquals(ParallelCompute.parallelReduceArray(new BigInteger[]{BigInteger.valueOf(42)}, BigInteger::multiply, 4), BigInteger.valueOf(42));
		} catch(InterruptedException e) {
		    System.out.println("Fehler: Reduce Operation sollte auch bei einem Element funktionieren");
		}

	}
	
	@Test
	public void parallelMapping() {
		Function<BigInteger,BigInteger> a = (r) -> r.multiply(r);
		Function<BigInteger,BigInteger>[] funcs = new Function[100];
		BigInteger[] data = new BigInteger[100];
		for(int i = 0; i< 100; i++) {
			data[i] = BigInteger.valueOf(i+1);
			funcs[i] = a;
		}
		BigInteger[] expected = new BigInteger[100];
		for(int i = 0; i< 100; i++) {
			expected[i] = BigInteger.valueOf((i+1)*(i+1));
		}
		try {
			BigInteger[] actual = ParallelCompute.parallelComputeFunctions(data, funcs, 4);
			assertArrayEquals(expected, actual);
			ParallelCompute.parallelComputeFunctions(data, null, 4);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NullPointerException e) {
			System.out.println("Should have happend");
		}
		
		data = new BigInteger[2];
		try {
			ParallelCompute.parallelComputeFunctions(data, funcs, 4);
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			System.out.println("Should have happend");
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}

