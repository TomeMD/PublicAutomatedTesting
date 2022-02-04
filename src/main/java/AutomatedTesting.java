import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.chrome.ChromeDriver;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;


public class AutomatedTesting {

    private static Map<String, String> map = new HashMap<String,String>() {{
        put("11111111X", "numbers_john.txt");
        put("22222222Y", "numbers_mark.txt");
    }};

    private static Map<String, Integer> mapCounters = new HashMap<String, Integer>() {{
        put("11111111X", 0);
        put("22222222Y", 0);
    }};

    private static boolean cambiarTarifa(WebDriver driver, WebElement slider_handle, String numero, boolean first)
            throws InterruptedException, InvalidRateException {
        if (!first)
            driver.findElement(By.className("myChange")).click();
        Actions actionProvider = new Actions(driver);
        // Change rate
        if (slider_handle.getAttribute("style").equals("left: 20%;")) {
            actionProvider.dragAndDropBy(slider_handle, 100, 0).perform();
        } else if (slider_handle.getAttribute("style").equals("left: 40%;")) {
            actionProvider.dragAndDropBy(slider_handle, -100, 0).perform();
        } else {
            throw new InvalidRateException(numero);
        }

        TimeUnit.SECONDS.sleep((long) 1.5);
        driver.findElement(By.className("buttonTotalTarifa")).click();
        TimeUnit.SECONDS.sleep((long) 1.5);
        driver.findElement(By.className("confirmar")).click();
        TimeUnit.SECONDS.sleep((long) 1.5);
        try {
            driver.findElement(By.className("msg_success"));
        } catch (NoSuchElementException e) {
            return false;
        }
        return true;
    }

    public static void main(String[] s) throws Exception {

        System.setProperty("webdriver.chrome.driver",System.getProperty("user.dir") + "/src/main/resources/ChromeDriver97/chromedriver.exe");
        WebDriver driver;
        JavascriptExecutor exec;
        BufferedReader br_dni = null;
        Scanner keyboard = new Scanner(System.in);

        try {
            // DNIs File
            br_dni = new BufferedReader(new FileReader(System.getProperty("user.dir") + "/src/main/resources/DNIs.txt"));
            String dni = br_dni.readLine();

            while (dni != null) {

                String passwd = null;

                System.out.print("Introduce la contraseña para el DNI "+dni+": ");
                passwd = keyboard.nextLine();
                if (passwd == null) throw new InvalidPasswordException("null");

                driver = new ChromeDriver();
                exec = (JavascriptExecutor) driver;
                driver.get("https://www.simyo.es/");

                // Accept cookies
                driver.findElement(By.id("aceptarCookies")).click();

                // Login
                driver.findElement(By.id("areaPersonal")).click();
                driver.findElement(By.id("tab_login_user")).sendKeys(dni);
                driver.findElement(By.id("tab_login_password")).sendKeys(passwd);
                driver.findElement(By.id("button")).click();

                // Numbers File
                String nombreFichero = System.getProperty("user.dir") + "/src/main/resources/"+map.get(dni);
                BufferedReader br = null;

                try {

                    br = new BufferedReader(new FileReader(nombreFichero));
                    String numero = br.readLine();
                    boolean success_change;

                    while (numero != null) {
                        exec.executeScript("document.getElementById('msisdn').setAttribute('value', '" + numero + "');");
                        driver.findElement(By.xpath("//*[@id=\"selectMsisdnForm\"]")).submit();
                        driver.findElement(By.className("myChange")).click();

                        WebElement slider_handle = driver.findElement(By.className("ui-slider-handle"));
                        success_change = AutomatedTesting.cambiarTarifa(driver, slider_handle, numero, true);
                        // Si la tarifa no se ha cambiado correctamente se intenta de nuevo
                        while (!success_change)
                            success_change = AutomatedTesting.cambiarTarifa(driver, slider_handle, numero, false);
                        //Se añade la tarifa cambiada al contador
                        mapCounters.put(dni, mapCounters.get(dni)+1);
                        //Leer la siguiente línea
                        numero = br.readLine();
                    }
                } catch (FileNotFoundException e) {
                    System.out.println("Error: Fichero no encontrado");
                    System.out.println(e.getMessage());
                } catch (Exception e) {
                    System.out.println("Error de lectura del fichero");
                    System.out.println(e.getMessage());
                } finally {
                    TimeUnit.SECONDS.sleep((long) 1.5);
                    try {
                        if (br != null)
                            br.close();
                        driver.close();
                    } catch (Exception e) {
                        System.out.println("Error al cerrar el fichero");
                        System.out.println(e.getMessage());
                    }
                }
                System.out.println("Se han cambiado "+mapCounters.get(dni)+" tarifas para el DNI "+dni);
                dni = br_dni.readLine();
            }
        } catch (FileNotFoundException e) {
            System.out.println("Error: Fichero no encontrado");
            System.out.println(e.getMessage());
        } catch (Exception e) {
            System.out.println("Error de lectura del fichero");
            System.out.println(e.getMessage());
        } finally {
            try {
                if (br_dni != null)
                    br_dni.close();
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
