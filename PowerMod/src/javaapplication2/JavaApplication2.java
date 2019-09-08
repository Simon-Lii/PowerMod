/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javaapplication2;

import java.util.Scanner;

/**
 *
 * @author Simon Li
 */
public class JavaApplication2 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        boolean prime_number_bool, mod_template;
        int base, power, mod;
        Scanner user_input = new Scanner(System.in);

        System.out.println("Base:");
        base = user_input.nextInt();
        System.out.println("Power:");
        power = user_input.nextInt();
        System.out.println("mod:");
        mod = user_input.nextInt();

        mod_template = isTemplate(mod);
        
        if (prime_number_bool = isPrime(mod)) {
            fermat_little_theorem(base, power, mod);
        }
        else if (mod_template == false) {
            weak_power_mod_function(base, power, mod);
            System.out.println("unique? "+mod_template);
        }
        
        else {
            int mod_prime_factors_length = prime_factorization_length(mod);
            int mod_prime_factors[] = prime_factorization(mod, mod_prime_factors_length);

            int uniqueFactors[], uniqueFactors_length;
            
            uniqueFactors_length= fc_uniqueFactorLength(mod_prime_factors);
            uniqueFactors = fc_uniqueFactor(mod_prime_factors, uniqueFactors_length);
            
            int smt_array[] = new int[uniqueFactors.length];
            for (int i = 0; i < uniqueFactors.length; i++) {
                smt_array[i] = flt_smt(base, power, uniqueFactors[i]);
            }
            Chinese_remainder_theorem(smt_array, uniqueFactors);

        }

    }

    private static boolean isPrime(int num) {
        if (num > 2 && num % 2 == 0) {
            return false;
        }
        int top = (int) Math.sqrt(num) + 1;
        for (int i = 3; i < top; i += 2) {
            if (num % i == 0) {
                return false;
            }
        }
        return true;
    }

    private static int[] prime_factorization(int number, int length) {
        int mod_prime_factors_array[] = new int[length];
        int mod_prime_factors_array_index = 0;
        for (int i = 2; i < number; i++) {
            while (number % i == 0) {
                mod_prime_factors_array[mod_prime_factors_array_index] = i;
                mod_prime_factors_array_index++;
                number = number / i;
            }

        }

        if (number > 2) {
            mod_prime_factors_array[mod_prime_factors_array_index] = number;
        }
        return mod_prime_factors_array;
    }

    private static int prime_factorization_length(int number) {
        int mod_prime_factors_array_length = 0;
        for (int i = 2; i < number; i++) {
            while (number % i == 0) {
                mod_prime_factors_array_length++;
                number = number / i;
            }
        }
        if (number > 2) {
            mod_prime_factors_array_length++;
        }
        return mod_prime_factors_array_length;
    }

    private static void fermat_little_theorem(int base, int power, int prime_num_mod) {
        int flt_power_factor;
        flt_power_factor = prime_num_mod - 1;

        int flt_power = power / flt_power_factor;
        int new_power;
        new_power = power - (flt_power_factor * flt_power);
        System.out.println("");
        System.out.println("Remainder is: " + (int) Math.round((Math.pow(base, new_power)) % prime_num_mod));
    }

    private static int flt_smt(int base, int power, int prime_num_mod) {
        int flt_power_factor;
        flt_power_factor = prime_num_mod - 1;

        int flt_power = power / flt_power_factor;
        int new_power;
        new_power = power - (flt_power_factor * flt_power);
        return (int) Math.round((Math.pow(base, new_power)) % prime_num_mod);
    }

    private static void Chinese_remainder_theorem(int[] smt_array, int[] mod_prime_factors) {
        if (mod_prime_factors.length == 1) {
            System.out.println("Remainder is: " + smt_array[0]);
        } else {
            int product_mods = 1;

            for (int i = 0; i < smt_array.length; i++) {
                product_mods *= mod_prime_factors[i];
            }
            int partial_product_smt[] = new int[smt_array.length];

            for (int i = 0; i < smt_array.length; i++) {
                partial_product_smt[i] = product_mods / mod_prime_factors[i];
            }
            // multiplicative inverse of smt_array[i] modulo partial_product_smt[i]:

            int inverse_smt_array[] = new int[smt_array.length];

            for (int i = 0; i < smt_array.length; i++) {
                inverse_smt_array[i] = computeInverse(partial_product_smt[i], mod_prime_factors[i]);
            }

            int finalSum = 0;

            for (int i = 0; i < smt_array.length; i++) {
                finalSum += partial_product_smt[i] * inverse_smt_array[i] * smt_array[i];
            }
            int reduced_finalSum;
            reduced_finalSum = finalSum % product_mods;
            System.out.println("Remainder is: " + reduced_finalSum);
        }
    }

    public static int computeInverse(int a, int b) {
        int m = b, t, q;
        int x = 0, y = 1;

        if (b == 1) {
            return 0;
        }

        // Apply extended Euclid Algorithm 
        while (a > 1) {
            // q is quotient 
            q = a / b;
            t = b;

            // now proceed same as Euclid's algorithm 
            b = a % b;
            a = t;
            t = x;
            x = y - q * x;
            y = t;
        }

        // Make x1 positive 
        if (y < 0) {
            y += m;
        }

        return y;
    }

    private static int[] fc_uniqueFactor(int[] PrimeFactors, int uniqueFactor_length) {
        int uniqueFactors[] = new int[uniqueFactor_length];
        int array_index=0;

        for (int i = 0; i < uniqueFactor_length; i++) {
            boolean isUnique = true;
            if (i > 0) {
                int uniquenessCount = 0;
                for (int j = 0; j <= i && isUnique; j++) {
                    if (uniqueFactors[array_index-1] == PrimeFactors[j]) {
                        uniquenessCount++;
                    }
                    if (uniquenessCount == 2) {
                        isUnique = false;
                    }
                }
            }
            if (isUnique) {
                uniqueFactors[array_index] = PrimeFactors[i];
                array_index++;
            }
            
        }
        return uniqueFactors;
    }

    private static int fc_uniqueFactorLength(int[] PrimeFactors) {
        int array_length_count=0;
        int uniqueFactors[]=new int [PrimeFactors.length];
        for (int i = 0; i < PrimeFactors.length; i++) {
            boolean isUnique = true;
            
            if (i > 0) {
                int uniquenessCount = 0;
                for (int j = 0; j < i+1 && isUnique; j++) { //fix this shit
                    if (uniqueFactors[array_length_count-1] == PrimeFactors[j]) {
                        uniquenessCount++;
                    }
                    if (uniquenessCount == 2) {
                        isUnique = false;
                    }
                }
            }
            if (isUnique) {
                uniqueFactors[array_length_count]= PrimeFactors[i];
                array_length_count++;
            }

        }
    return array_length_count;}

    private static boolean isSmallPower(int power) {
        int powerThreshhold=80;
        boolean isSmallPower=false;
        if (power < powerThreshhold) {
            isSmallPower=true;
        }
        return isSmallPower;
    }

    private static boolean isTemplate(int number) {
        int length = prime_factorization_length (number);
        int factors[] = prime_factorization(number,length);
        int uniqueFactors[] = new int [length];
        int array_index=0;
        boolean isUnique = true;

        for (int i = 0; i < length && isUnique; i++) {
            if (i > 0) {
                int uniquenessCount = 0;
                for (int j = 0; j <= i && isUnique; j++) {
                    if (uniqueFactors[array_index-1] == factors[j]) {
                        uniquenessCount++;
                    }
                    if (uniquenessCount == 2) {
                        isUnique = false;
                    }
                }
            }
            if (isUnique) {
                uniqueFactors[array_index]=factors[i];
                array_index++;
            }
            
        }
        return isUnique;
    }
}