package challenge;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Restaurant {

    private final PaymentReceiptGenerator paymentReceiptGenerator;
    private final List<Menu> menus;
    private Boolean hasOrdered = false;
    private final Scanner sc;
    public Restaurant(PaymentReceiptGenerator paymentReceiptGenerator, Scanner sc) {
        this.sc = sc;
        this.paymentReceiptGenerator = paymentReceiptGenerator;
        this.menus = new ArrayList<>(Arrays.asList(
                new FoodItem("Nasi goreng", 20000, false),
                new FoodItem("Mie goreng", 15000, false),
                new FoodItem("Bubur ayam", 10000, false),
                new FoodItem("Soto ayam", 17000, false),
                new FoodItem("Gado-gado", 10000, false)
        ));
    }

    public Restaurant(PaymentReceiptGenerator paymentReceiptGenerator, List<Menu> menus, Scanner sc) {
        this.sc = sc;
        this.paymentReceiptGenerator = paymentReceiptGenerator;
        this.menus = menus;
    }

    public void showOrderMenu() {
        System.out.println("==========================");
        System.out.println("Selamat datang di BinarFud");
        System.out.println("==========================\n");
        System.out.println("Silahkan pilih makanan : ");
        for (int i = 0; i < menus.size(); i++) {
            System.out.println(i+1 + ". " + menus.get(i).getFood() + "\t\t" + menus.get(i).getPrice());
        }
        System.out.println("99. Pesan dan bayar");
        System.out.println("0. Keluar aplikasi\n");

        while(true) {
            System.out.print("=> ");
            if (sc.hasNextInt()) {
                int selectedMenu = sc.nextInt();
                if (selectedMenu == 99) {
                    if (hasOrdered) {
                        showOrderConfirmationMenu();
                        break;
                    } else {
                        System.out.println("Pilih menu terlebih dahulu!");
                    }
                } else if (selectedMenu > menus.size() || selectedMenu < 0) {
                    System.out.println("Silahkan pilih menu 1 - " + menus.size());
                } else if(selectedMenu == 0){
                    break;
                } else {
                    System.out.println();
                    showOrderQuantity(selectedMenu-1);
                    break;
                }
            } else {
                System.out.println("Masukkan input yang benar");
                sc.next();
            }
        }
    }

    private void showOrderQuantity(int selectedMenu) {
        System.out.println("==========================");
        System.out.println("Berapa pesanan anda");
        System.out.println("==========================\n");
        System.out.println(menus.get(selectedMenu).getFood() + "\t| " + menus.get(selectedMenu).getPrice());
        System.out.println("(input 0 untuk kembali)\n");
        FoodItem menu = (FoodItem) menus.get(selectedMenu);
        while(true) {
            System.out.print("qty => ");
            if (sc.hasNextInt()) {
                int qty = sc.nextInt();
                if (qty == 0) {
                    break;
                } else if (qty < 1) {
                    System.out.println("==========================");
                    System.out.println("Minimal 1 jumlah \npesanan!");
                    System.out.println("==========================");
                } else {
                    menu.setQuantity(qty);
                    menu.setIsOrdered(true);
                    break;
                }
            } else {
                System.out.println("Masukkan input yang benar");
                sc.next();
            }
        }
        hasOrdered = true;
        showOrderMenu();
    }


    private void showOrderConfirmationMenu() {
        System.out.println("\n==========================");
        System.out.println("Konfirmasi pembayaran");
        System.out.println("==========================\n");
        int totalQty = 0;
        int totalPrice = 0;
        for (Menu menu : menus) {
            FoodItem item = (FoodItem) menu;
            if (item.getIsOrdered()) {
                String food = item.getFood();
                int quantity = item.getQuantity();
                int totalPricePerMenu = item.getTotalPrice();
                System.out.println(food + "\t" + quantity + "\t" + totalPricePerMenu);
                totalQty += quantity;
                totalPrice += totalPricePerMenu;
            }
        }

        System.out.println("----------------------+");
        System.out.println("Total" + "\t\t" + totalQty + "\t" + totalPrice + "\n");

        System.out.println("1. Konfirmasi pembayaran");
        System.out.println("2. Kembali ke menu utama");
        System.out.println("0. Keluar aplikasi\n");

        while(true) {
            System.out.print("=> ");
            if (sc.hasNextInt()) {
                int input = sc.nextInt();
                if (input == 0) break;
                else if (input == 1) {
                    try {
                        this.paymentReceiptGenerator.generatePaymentReceipt(totalQty, totalPrice, menus);
                        this.paymentReceiptGenerator.readPaymentReceiptFromFile();
                    } catch (IOException e) {
                        System.err.println("Terjadi kesalahan saat menyimpan struk pembayaran: " + e.getMessage());
                    }
                    break;
                } else if (input == 2) {
                    showOrderMenu();
                    break;
                } else {
                    System.out.println("Pilih angka 0 - 2");
                }
            } else {
                System.out.println("Masukkan input yang benar");
                sc.next();
            }
        }
    }
}
