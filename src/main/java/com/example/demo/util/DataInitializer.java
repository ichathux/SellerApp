package com.example.demo.util;

import com.example.demo.dto.SignUpDto;
import com.example.demo.dto.UserDto;
import com.example.demo.model.SellerDetails;
import com.example.demo.model.User;
import com.example.demo.model.inventory.Category;
import com.example.demo.model.inventory.SubCategory;
import com.example.demo.repository.SellerDetailsRepository;
import com.example.demo.repository.inventory.CategoryRepository;
import com.example.demo.repository.inventory.SubCategoryRepository;
import com.example.demo.service.AuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class DataInitializer implements CommandLineRunner {
    @Autowired
    private SellerDetailsRepository sellerDetailsRepository;
    @Autowired
    private SubCategoryRepository subCategoryRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private AuthService authService;

    @Override
    public void run(String... args) throws Exception {
        System.out.println("running command line runner ");
        addCategories();
        addSubCategories();
        addAdminUser();
    }

    private void addAdminUser() {
        try{
            SignUpDto user = new SignUpDto();
            user.setUsername("admin@email.com");
            user.setPassword("password".toCharArray());
            user.setBusinessName("Test Business");
            user.setContact("0123456789");
            user.setCountry("Sri Lanka");
            authService.register(user).getBody();
        }catch (Exception e){
            log.error(e.getMessage());
        }

    }

    private void addCategories() {
        List<Category> categories = List.of(new Category("Electronic devices") ,
                new Category("Electronic accessories") ,
                new Category("Tv & home appliances") ,
                new Category("Health & beauty") ,
                new Category("Babies & toys") ,
                new Category("Groceries & pets") ,
                new Category("Home & lifestyle") ,
                new Category("Women’s fashion") ,
                new Category("Men’s fashion") ,
                new Category("Watches & accessories") ,
                new Category("Sports & outdoor") ,
                new Category("Automotive & motorbike"));
        for (Category category : categories) {
            try {
                categoryRepository.findCategoryByName(category.getName())
                        .ifPresentOrElse((c) -> {
                            System.out.println(category.getName() + " already exists on db");
                            throw new RuntimeException("Category adding fail. Already Exist " + c.getName());
                        } , () -> {
                            categoryRepository.insert(category);
                        });
            } catch (Exception e) {
                log.error(e.getMessage());
            }
        }
    }

    private void addSubCategories() {
        List<SubCategory> subCategories = List.of(
                new SubCategory("Tablets" , new Category("Electronic devices")) ,
                new SubCategory("Laptops" , new Category("Electronic devices")) ,
                new SubCategory("Desktops" , new Category("Electronic devices")) ,
                new SubCategory("Gaming Consoles" , new Category("Electronic devices")) ,
                new SubCategory("Cameras" , new Category("Electronic devices")) ,
                new SubCategory("Refurbished Devices" , new Category("Electronic devices")) ,
                new SubCategory("Tablets" , new Category("Electronic devices")) ,
                new SubCategory("Mobile" , new Category("Electronic devices")) ,
                new SubCategory("Mobile Accessories" , new Category("Electronic accessories")) ,
                new SubCategory("Audio" , new Category("Electronic accessories")) ,
                new SubCategory("Wearable" , new Category("Electronic accessories")) ,
                new SubCategory("Console Accessories" , new Category("Electronic accessories")) ,
                new SubCategory("Camera Accessories" , new Category("Electronic accessories")) ,
                new SubCategory("Computer Accessories" , new Category("Electronic accessories")) ,
                new SubCategory("Storage" , new Category("Electronic accessories")) ,
                new SubCategory("Printers" , new Category("Electronic accessories")) ,
                new SubCategory("Computer Components" , new Category("Electronic accessories")) ,
                new SubCategory("Network Components" , new Category("Electronic accessories")) ,
                new SubCategory("TV & Video Devices" , new Category("Tv & home appliances")) ,
                new SubCategory("Irons" , new Category("Tv & home appliances")) ,
                new SubCategory("Vacuums & Floor Care" , new Category("Tv & home appliances")) ,
                new SubCategory("Cooling & Heating" , new Category("Tv & home appliances")) ,
                new SubCategory("Kitchen Appliances" , new Category("Tv & home appliances")) ,
                new SubCategory("Sewing Machine" , new Category("Tv & home appliances")) ,
                new SubCategory("Gas Burners" , new Category("Tv & home appliances")) ,
                new SubCategory("Refrigerators" , new Category("Tv & home appliances")) ,
                new SubCategory("Washing Machines" , new Category("Tv & home appliances")) ,
                new SubCategory("Home Audio" , new Category("Tv & home appliances")) ,
                new SubCategory("TV Accessories" , new Category("Tv & home appliances")) ,
                new SubCategory("Bath & Body" , new Category("Health & beauty")) ,
                new SubCategory("Beauty Tools" , new Category("Health & beauty")) ,
                new SubCategory("Fragrances" , new Category("Health & beauty")) ,
                new SubCategory("Hair Care" , new Category("Health & beauty")) ,
                new SubCategory("Makeup" , new Category("Health & beauty")) ,
                new SubCategory("Men\"s Care" , new Category("Health & beauty")) ,
                new SubCategory("Personal Care" , new Category("Health & beauty")) ,
                new SubCategory("Skin Care" , new Category("Health & beauty")) ,
                new SubCategory("Food Supplements" , new Category("Health & beauty")) ,
                new SubCategory("Medical Supplies" , new Category("Health & beauty")) ,
                new SubCategory("Sexual Wellness" , new Category("Health & beauty")) ,
                new SubCategory("Baby Gear" , new Category("Babies & toys")) ,
                new SubCategory("Baby Personal Care" , new Category("Babies & toys")) ,
                new SubCategory("Maternity Care" , new Category("Babies & toys")) ,
                new SubCategory("Baby Safety" , new Category("Babies & toys")) ,
                new SubCategory("Baby Health Care" , new Category("Babies & toys")) ,
                new SubCategory("Pacifiers & Accessories" , new Category("Babies & toys")) ,
                new SubCategory("Gifts" , new Category("Babies & toys")) ,
                new SubCategory("Clothing & Accessories" , new Category("Babies & toys")) ,
                new SubCategory("Diapering & Potty" , new Category("Babies & toys")) ,
                new SubCategory("Feeding" , new Category("Babies & toys")) ,
                new SubCategory("Nursery" , new Category("Babies & toys")) ,
                new SubCategory("Baby & Toddler Toys" , new Category("Babies & toys")) ,
                new SubCategory("Beverages" , new Category("Groceries & pets")) ,
                new SubCategory("Breakfast, Choco & Snacks" , new Category("Groceries & pets")) ,
                new SubCategory("Food Staples" , new Category("Groceries & pets")) ,
                new SubCategory("Laundry & Household" , new Category("Groceries & pets")) ,
                new SubCategory("Cat" , new Category("Groceries & pets")) ,
                new SubCategory("Dog" , new Category("Groceries & pets")) ,
                new SubCategory("Fresh Produce" , new Category("Groceries & pets")) ,
                new SubCategory("Fish" , new Category("Groceries & pets")) ,
                new SubCategory("Bath" , new Category("Home & lifestyle")) ,
                new SubCategory("Bedding" , new Category("Home & lifestyle")) ,
                new SubCategory("Decor" , new Category("Home & lifestyle")) ,
                new SubCategory("Furniture" , new Category("Home & lifestyle")) ,
                new SubCategory("Kitchen & Dining" , new Category("Home & lifestyle")) ,
                new SubCategory("Lighting" , new Category("Home & lifestyle")) ,
                new SubCategory("Laundry & Cleaning" , new Category("Home & lifestyle")) ,
                new SubCategory("Tools, DIY & Outdoor" , new Category("Home & lifestyle")) ,
                new SubCategory("Stationery & Craft" , new Category("Home & lifestyle")) ,
                new SubCategory("Media, Music & Books" , new Category("Home & lifestyle")) ,
                new SubCategory("Charity & Donation" , new Category("Home & lifestyle")) ,
                new SubCategory("Clothing" , new Category("Women’s fashion")) ,
                new SubCategory("Accessories" , new Category("Women’s fashion")) ,
                new SubCategory("Lingerie, Sleep & Lounge" , new Category("Women’s fashion")) ,
                new SubCategory("Shoes" , new Category("Women’s fashion")) ,
                new SubCategory("Bags" , new Category("Women’s fashion")) ,
                new SubCategory("Girl\"s Fashion" , new Category("Women’s fashion")) ,
                new SubCategory("Clothing" , new Category("Men’s fashion")) ,
                new SubCategory("Men\"s Bags" , new Category("Men’s fashion")) ,
                new SubCategory("Shoes" , new Category("Men’s fashion")) ,
                new SubCategory("Accessories" , new Category("Men’s fashion")) ,
                new SubCategory("Underwear" , new Category("Men’s fashion")) ,
                new SubCategory("Boy\"s Fashion" , new Category("Men’s fashion")) ,
                new SubCategory("Men\"s Watches" , new Category("Watches & accessories")) ,
                new SubCategory("Women Watches" , new Category("Watches & accessories")) ,
                new SubCategory("Unisex Watches" , new Category("Watches & accessories")) ,
                new SubCategory("Kid Watches" , new Category("Watches & accessories")) ,
                new SubCategory("Sunglasses" , new Category("Watches & accessories")) ,
                new SubCategory("Eyeglasses" , new Category("Watches & accessories")) ,
                new SubCategory("Men Fashion Jewellery" , new Category("Watches & accessories")) ,
                new SubCategory("Women Fashion Jewellery" , new Category("Watches & accessories")) ,
                new SubCategory("Jewellery" , new Category("Watches & accessories")) ,
                new SubCategory("Men Shoes & Clothing" , new Category("Sports & outdoor")) ,
                new SubCategory("Women Shoes & Clothing" , new Category("Sports & outdoor")) ,
                new SubCategory("Outdoor Recreation" , new Category("Sports & outdoor")) ,
                new SubCategory("Exercise & Fitness" , new Category("Sports & outdoor")) ,
                new SubCategory("Water Sports" , new Category("Sports & outdoor")) ,
                new SubCategory("Boxing & Martial Arts" , new Category("Sports & outdoor")) ,
                new SubCategory("Racket Sports" , new Category("Sports & outdoor")) ,
                new SubCategory("Team Sports" , new Category("Sports & outdoor")) ,
                new SubCategory("Water Bottles" , new Category("Sports & outdoor")) ,
                new SubCategory("Automotive" , new Category("Automotive & motorbike")) ,
                new SubCategory("Motorcycle" , new Category("Automotive & motorbike")) ,
                new SubCategory("Three-wheeler" , new Category("Automotive & motorbike")) ,
                new SubCategory("Automobile" , new Category("Automotive & motorbike")));

        for (SubCategory subCategory : subCategories) {
            try {
                subCategoryRepository.findSubCategoryByName(subCategory.getName()).ifPresentOrElse((s) -> {
                    System.out.println("Sub category already exists");
                    throw new RuntimeException("Sub category already exists " + s.getName());
                } , () -> {
                    categoryRepository.findCategoryByName(subCategory.getCategory().getName()).ifPresentOrElse((s) -> {
                        subCategory.setCategory(s);
                        subCategoryRepository.save(subCategory);
                    } , () -> {
                        System.out.println("Category not exists " + subCategory.getCategory().getName());
                        throw new RuntimeException("Category not exists " + subCategory.getCategory().getName());
                    });
                });
            } catch (Exception e) {
                log.error(e.getMessage());
            }

        }

    }
}
