package com.example.chronicdisease.config;

import com.example.chronicdisease.model.*;
import com.example.chronicdisease.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Component
public class DataInitializer implements CommandLineRunner {
    @Autowired
    private ChronicDiseaseRepository chronicDiseaseRepository;

    @Autowired
    private LatestResearchRepository latestResearchRepository;
    
    @Autowired
    private SeasonalTipRepository seasonalTipRepository;
    
    @Autowired
    private HealthHabitRepository healthHabitRepository;
    
    @Autowired
    private NutritionDietRepository nutritionDietRepository;
    
    @Autowired
    private ChronicDiseaseMedicationRepository chronicDiseaseMedicationRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Override
    public void run(String... args) {
        // Initialize chronic disease data
        if (chronicDiseaseRepository.count() == 0) {
            // Diabetes
            ChronicDisease diabetes = new ChronicDisease();
            diabetes.setName("Diabetes");
            diabetes.setDescription("Diabetes is a metabolic disease characterized by high blood sugar. Chronic high blood sugar can lead to chronic damage and dysfunction of various tissues, especially eyes, kidneys, heart, blood vessels, and nerves.");
            diabetes.setSymptoms("Excessive thirst, frequent urination, increased hunger, weight loss, fatigue, blurred vision, slow-healing wounds");
            diabetes.setCauses("Genetic factors, environmental factors, lifestyle, obesity, lack of exercise, hypertension, high cholesterol, etc.");
            diabetes.setPrevention("Healthy diet, regular exercise, weight control, regular check-ups, smoking cessation, limited alcohol, maintaining a good state of mind");
            diabetes.setTreatment("Medication, diet control, exercise therapy, blood glucose monitoring, insulin therapy, prevention and treatment of complications");
            diabetes.setRiskFactors(Arrays.asList("Obesity", "Family History", "Lack of Exercise", "Hypertension", "High Cholesterol", "Age > 45", "Gestational Diabetes History"));
            diabetes.setImageUrl("/images/diabetes.jpg");
            diabetes.setOrder(1);
            chronicDiseaseRepository.save(diabetes);
            
            // Hypertension
            ChronicDisease hypertension = new ChronicDisease();
            hypertension.setName("Hypertension");
            hypertension.setDescription("Hypertension is a disease characterized by persistently elevated arterial blood pressure and is a major risk factor for cardiovascular and cerebrovascular diseases.");
            hypertension.setSymptoms("Headache, dizziness, palpitations, tinnitus, insomnia, difficulty concentrating, memory loss");
            hypertension.setCauses("Genetic factors, high salt diet, obesity, stress, lack of exercise, smoking, alcohol, aging");
            hypertension.setPrevention("Low salt diet, weight control, smoking cessation, limited alcohol, regular exercise, maintaining a good state of mind, regular blood pressure monitoring");
            hypertension.setTreatment("Medication, lifestyle adjustments, blood pressure monitoring, prevention and treatment of complications");
            hypertension.setRiskFactors(Arrays.asList("Advanced Age", "Family History", "High Salt Diet", "Obesity", "Lack of Exercise", "Smoking", "Alcohol", "High Stress"));
            hypertension.setImageUrl("/images/hypertension.jpg");
            hypertension.setOrder(2);
            chronicDiseaseRepository.save(hypertension);
            
            // Heart Disease
            ChronicDisease heartDisease = new ChronicDisease();
            heartDisease.setName("Heart Disease");
            heartDisease.setDescription("Heart disease is a group of diseases that affect the structure and function of the heart, including coronary heart disease, cardiomyopathy, and arrhythmia.");
            heartDisease.setSymptoms("Chest pain, chest tightness, palpitations, shortness of breath, fatigue, dizziness, fainting, lower limb edema");
            heartDisease.setCauses("Hypertension, high cholesterol, diabetes, smoking, obesity, lack of exercise, stress, genetic factors");
            heartDisease.setPrevention("Control blood pressure, cholesterol, blood sugar, quit smoking, healthy diet, regular exercise, weight control, maintaining a good state of mind");
            heartDisease.setTreatment("Medication, surgery, lifestyle adjustments, rehabilitation training, regular follow-up");
            heartDisease.setRiskFactors(Arrays.asList("Hypertension", "High Cholesterol", "Diabetes", "Smoking", "Obesity", "Lack of Exercise", "Family History", "Aging"));
            heartDisease.setImageUrl("/images/heart-disease.jpg");
            heartDisease.setOrder(3);
            chronicDiseaseRepository.save(heartDisease);
        }
        
        // Initialize latest research data
        if (latestResearchRepository.count() == 0) {
            // Research 1
            LatestResearch research1 = new LatestResearch();
            research1.setTitle("Research Progress of New Diabetes Treatment Drugs");
            research1.setContent("Latest research shows that the new GLP-1 receptor agonist has significant effects in controlling blood sugar and reducing weight, and can reduce the risk of cardiovascular events.");
            research1.setSource("Chinese Journal of Diabetes");
            research1.setAuthor("Prof. Zhang");
            research1.setPublishDate(new Date());
            research1.setImageUrl("/images/research1.jpg");
            research1.setLink("https://example.com/research1");
            research1.setOrder(1);
            latestResearchRepository.save(research1);
            
            // Research 2
            LatestResearch research2 = new LatestResearch();
            research2.setTitle("Lifestyle Intervention Research in Hypertension Patients");
            research2.setContent("A 5-year study showed that comprehensive lifestyle intervention can significantly reduce blood pressure levels and the incidence of cardiovascular events in hypertension patients.");
            research2.setSource("Chinese Journal of Hypertension");
            research2.setAuthor("Prof. Li");
            research2.setPublishDate(new Date());
            research2.setImageUrl("/images/research2.jpg");
            research2.setLink("https://example.com/research2");
            research2.setOrder(2);
            latestResearchRepository.save(research2);
            
            // Research 3
            LatestResearch research3 = new LatestResearch();
            research3.setTitle("New Early Diagnosis Method for Heart Disease");
            research3.setContent("Researchers developed a new biomarker detection method that can predict heart disease risk in advance with an accuracy of over 90%.");
            research3.setSource("Chinese Journal of Cardiovascular Disease");
            research3.setAuthor("Prof. Wang");
            research3.setPublishDate(new Date());
            research3.setImageUrl("/images/research3.jpg");
            research3.setLink("https://example.com/research3");
            research3.setOrder(3);
            latestResearchRepository.save(research3);
            
            // Research 4
            LatestResearch research4 = new LatestResearch();
            research4.setTitle("Progress in Prevention and Treatment of Diabetes Complications");
            research4.setContent("Latest research found that strict blood sugar control can significantly reduce the risk of diabetes complications, especially retinopathy and nephropathy.");
            research4.setSource("Chinese Journal of Diabetes");
            research4.setAuthor("Prof. Zhao");
            research4.setPublishDate(new Date());
            research4.setImageUrl("/images/research4.jpg");
            research4.setLink("https://example.com/research4");
            research4.setOrder(4);
            latestResearchRepository.save(research4);
            
            // Research 5
            LatestResearch research5 = new LatestResearch();
            research5.setTitle("Exercise Prescription Research for Hypertension Patients");
            research5.setContent("Research shows that regular aerobic exercise can significantly reduce blood pressure levels and improve vascular function in hypertension patients.");
            research5.setSource("Chinese Journal of Hypertension");
            research5.setAuthor("Prof. Liu");
            research5.setPublishDate(new Date());
            research5.setImageUrl("/images/research5.jpg");
            research5.setLink("https://example.com/research5");
            research5.setOrder(5);
            latestResearchRepository.save(research5);
            
            // Research 6
            LatestResearch research6 = new LatestResearch();
            research6.setTitle("New Method for Heart Disease Rehabilitation Treatment");
            research6.setContent("A new heart rehabilitation program can significantly improve the quality of life and exercise capacity of heart disease patients and reduce re-admission rates.");
            research6.setSource("Chinese Journal of Cardiovascular Disease");
            research6.setAuthor("Prof. Chen");
            research6.setPublishDate(new Date());
            research6.setImageUrl("/images/research6.jpg");
            research6.setLink("https://example.com/research6");
            research6.setOrder(6);
            latestResearchRepository.save(research6);
            
            // Research 7
            LatestResearch research7 = new LatestResearch();
            research7.setTitle("New Progress in Nutritional Treatment of Diabetes");
            research7.setContent("Personalized nutritional treatment programs can significantly improve blood glucose control and quality of life for diabetes patients.");
            research7.setSource("Chinese Journal of Diabetes");
            research7.setAuthor("Prof. Sun");
            research7.setPublishDate(new Date());
            research7.setImageUrl("/images/research7.jpg");
            research7.setLink("https://example.com/research7");
            research7.setOrder(7);
            latestResearchRepository.save(research7);
            
            // Research 8
            LatestResearch research8 = new LatestResearch();
            research8.setTitle("Psychological Intervention Research for Hypertension Patients");
            research8.setContent("Psychological intervention can significantly improve the psychological state and blood pressure control effect of hypertension patients.");
            research8.setSource("Chinese Journal of Hypertension");
            research8.setAuthor("Prof. Zhou");
            research8.setPublishDate(new Date());
            research8.setImageUrl("/images/research8.jpg");
            research8.setLink("https://example.com/research8");
            research8.setOrder(8);
            latestResearchRepository.save(research8);
            
            // Research 9
            LatestResearch research9 = new LatestResearch();
            research9.setTitle("Remote Monitoring Research for Heart Disease Patients");
            research9.setContent("Remote monitoring systems can significantly improve treatment compliance and quality of life for heart disease patients.");
            research9.setSource("Chinese Journal of Cardiovascular Disease");
            research9.setAuthor("Prof. Wu");
            research9.setPublishDate(new Date());
            research9.setImageUrl("/images/research9.jpg");
            research9.setLink("https://example.com/research9");
            research9.setOrder(9);
            latestResearchRepository.save(research9);
            
            // Research 10
            LatestResearch research10 = new LatestResearch();
            research10.setTitle("New Early Screening Method for Diabetes Complications");
            research10.setContent("New screening methods can detect diabetes complications early and improve treatment effects.");
            research10.setSource("Chinese Journal of Diabetes");
            research10.setAuthor("Prof. Zheng");
            research10.setPublishDate(new Date());
            research10.setImageUrl("/images/research10.jpg");
            research10.setLink("https://example.com/research10");
            research10.setOrder(10);
            latestResearchRepository.save(research10);
            
            // Research 11
            LatestResearch research11 = new LatestResearch();
            research11.setTitle("Progress in Drug Treatment for Hypertension Patients");
            research11.setContent("New antihypertensive drugs can significantly increase the blood pressure control rate and reduce adverse reactions.");
            research11.setSource("Chinese Journal of Hypertension");
            research11.setAuthor("Prof. Ma");
            research11.setPublishDate(new Date());
            research11.setImageUrl("/images/research11.jpg");
            research11.setLink("https://example.com/research11");
            research11.setOrder(11);
            latestResearchRepository.save(research11);
            
            // Research 12
            LatestResearch research12 = new LatestResearch();
            research12.setTitle("Lifestyle Intervention Research for Heart Disease Patients");
            research12.setContent("Comprehensive lifestyle intervention can significantly improve the quality of life and prognosis of heart disease patients.");
            research12.setSource("Chinese Journal of Cardiovascular Disease");
            research12.setAuthor("Prof. Huang");
            research12.setPublishDate(new Date());
            research12.setImageUrl("/images/research12.jpg");
            research12.setLink("https://example.com/research12");
            research12.setOrder(12);
            latestResearchRepository.save(research12);
        }
        
        // Initialize seasonal health tips data
        if (seasonalTipRepository.count() == 0) {
            // Spring Tips
            SeasonalTip springTip1 = new SeasonalTip();
            springTip1.setSeason("spring");
            springTip1.setTitle("Managing Allergy Season");
            springTip1.setContent("Pollen increases in spring, which may worsen allergy symptoms and asthma. Reducing outdoor time, changing clothes after going out, and washing your hands and face can effectively reduce symptoms.");
            springTip1.setAdvice("Hypertension patients please note that some anti-allergy medications may affect blood pressure. Please consult a doctor before using them.");
            springTip1.setOrder(1);
            seasonalTipRepository.save(springTip1);
            
            SeasonalTip springTip2 = new SeasonalTip();
            springTip2.setSeason("spring");
            springTip2.setTitle("Spring Diet Adjustment");
            springTip2.setContent("In spring, you should choose fresh vegetables and fruits, foods rich in vitamins, such as asparagus, spinach, and strawberries. Appropriately increase protein intake and reduce heavy oil and salt foods.");
            springTip2.setAdvice("Diabetes patients can eat seasonal spring vegetables in moderation, but pay attention to controlling fruit intake to avoid blood sugar fluctuations.");
            springTip2.setOrder(2);
            seasonalTipRepository.save(springTip2);
            
            SeasonalTip springTip3 = new SeasonalTip();
            springTip3.setSeason("spring");
            springTip3.setTitle("Moderate Spring Exercise");
            springTip3.setContent("Spring is a good time to start outdoor activities. You can choose to walk, do Tai Chi, or do light aerobic exercise in the morning or evening, 3-5 times a week, 30 minutes each time.");
            springTip3.setAdvice("Heart disease patients should increase the amount of exercise gradually and avoid exercising when temperature changes are large.");
            springTip3.setOrder(3);
            seasonalTipRepository.save(springTip3);
            
            // Summer Tips
            SeasonalTip summerTip1 = new SeasonalTip();
            summerTip1.setSeason("summer");
            summerTip1.setTitle("Heatstroke Prevention and Cooling Measures");
            summerTip1.setContent("High summer temperatures can increase cardiovascular burden. Avoid going out from 10 AM to 4 PM, keep the room ventilated and cool, drink more water but avoid overly cold drinks.");
            summerTip1.setAdvice("Hypertension patients should avoid large temperature differences, pay attention to adapting to temperature differences when entering and leaving air-conditioned rooms to prevent blood pressure fluctuations.");
            summerTip1.setOrder(1);
            seasonalTipRepository.save(summerTip1);
            
            SeasonalTip summerTip2 = new SeasonalTip();
            summerTip2.setSeason("summer");
            summerTip2.setTitle("Summer Diet Recommendations");
            summerTip2.setContent("The summer diet should be light. You can eat more bitter melon, cucumber, tomatoes, and other vegetables, and watermelon, pears, and other fruits. Reduce high-fat, spicy, and stimulating foods.");
            summerTip2.setAdvice("When choosing fruits, diabetes patients should prioritize low-sugar fruits such as watermelon (in moderation), grapefruit, etc., and count them towards the total daily carbohydrate intake.");
            summerTip2.setOrder(2);
            seasonalTipRepository.save(summerTip2);
            
            SeasonalTip summerTip3 = new SeasonalTip();
            summerTip3.setSeason("summer");
            summerTip3.setTitle("Summer Medication Storage");
            summerTip3.setContent("High-temperature environments will affect medication stability. Please store medications in a cool, dry place, avoid direct sunlight and humid environments, and take care of medication storage when traveling.");
            summerTip3.setAdvice("Medications that need refrigeration, such as insulin, should be stored in a refrigerator at 2-8°C, but do not freeze.");
            summerTip3.setOrder(3);
            seasonalTipRepository.save(summerTip3);
            
            // Autumn Tips
            SeasonalTip autumnTip1 = new SeasonalTip();
            autumnTip1.setSeason("autumn");
            autumnTip1.setTitle("Autumn Dryness and Respiratory System");
            autumnTip1.setContent("The autumn climate is dry, which can easily cause throat discomfort and respiratory problems. It is recommended to maintain indoor humidity, drink more water, and use a humidifier if necessary.");
            autumnTip1.setAdvice("Patients with chronic bronchitis should pay special attention to dryness prevention. They can adjust through hot foot baths and appropriately drinking warm tea.");
            autumnTip1.setOrder(1);
            seasonalTipRepository.save(autumnTip1);
            
            SeasonalTip autumnTip2 = new SeasonalTip();
            autumnTip2.setSeason("autumn");
            autumnTip2.setTitle("Autumn Diet Adjustment");
            autumnTip2.setContent("In autumn, you should increase foods that nourish Yin and moisten the lungs, such as pears, lilies, and white fungus. Eat an appropriate amount of nuts to supplement essential fatty acids.");
            autumnTip2.setAdvice("Heart disease patients should pay attention to controlling nut intake, choose low-salt or unsalted nuts, and eat no more than a small handful every day.");
            autumnTip2.setOrder(2);
            seasonalTipRepository.save(autumnTip2);
            
            SeasonalTip autumnTip3 = new SeasonalTip();
            autumnTip3.setSeason("autumn");
            autumnTip3.setTitle("Autumn Emotional Management");
            autumnTip3.setContent("In autumn, the temperature difference between day and night is large, and sunshine decreases. Some people may experience low mood. Maintain a regular schedule, increase social activities, and maintain a positive attitude.");
            autumnTip3.setAdvice("Patients taking antidepressants for a long time should communicate with their doctor in time if they have emotional fluctuations when the season changes. Do not adjust the medication dosage yourself.");
            autumnTip3.setOrder(3);
            seasonalTipRepository.save(autumnTip3);
            
            // Winter Tips
            SeasonalTip winterTip1 = new SeasonalTip();
            winterTip1.setSeason("winter");
            winterTip1.setTitle("Key Points for Cold Prevention and Warmth");
            winterTip1.setContent("Cold weather in winter easily induces cardiovascular and cerebrovascular diseases. Keep warm before going out, especially the head, neck, and feet. When suddenly entering a warm environment, you should unbutton your coat to adapt first.");
            winterTip1.setAdvice("Coronary heart disease patients should reduce outdoor activities in winter, especially in the early morning and when the temperature drops suddenly. Take emergency medicine before going out.");
            winterTip1.setOrder(1);
            seasonalTipRepository.save(winterTip1);
            
            SeasonalTip winterTip2 = new SeasonalTip();
            winterTip2.setSeason("winter");
            winterTip2.setTitle("Winter Exercise Guide");
            winterTip2.setContent("In winter, you still need to maintain appropriate exercise. You can choose indoor activities such as Tai Chi and yoga. Warm up fully before outdoor sports and choose a sunny afternoon.");
            winterTip2.setAdvice("Arthritis patients can apply hot compresses to joints before exercising in winter to reduce stiffness. The exercise intensity should be gentle, and avoid vigorous exercise and exercising in cold environments.");
            winterTip2.setOrder(2);
            seasonalTipRepository.save(winterTip2);
            
            SeasonalTip winterTip3 = new SeasonalTip();
            winterTip3.setSeason("winter");
            winterTip3.setTitle("Holiday Diet Control");
            winterTip3.setContent("There are many holidays in winter, and it's easy to overeat. It is recommended to control holiday food intake, maintain regular three meals, and avoid overeating and excessive drinking.");
            winterTip3.setAdvice("When attending gatherings, diabetes patients can eat a small amount in advance to avoid attending on an empty stomach; choose low-sugar, high-fiber foods, and strictly control dessert intake.");
            winterTip3.setOrder(3);
            seasonalTipRepository.save(winterTip3);
        }
        
        // Initialize health habit data
        if (healthHabitRepository.count() == 0) {
            // Exercise Category
            HealthHabit exercise1 = new HealthHabit();
            exercise1.setTitle("Daily Walking");
            exercise1.setContent("Insisting on walking for 30 minutes every day can improve cardiopulmonary function and reduce blood pressure and blood sugar levels. It is recommended to choose parks or trails with fresh air.");
            exercise1.setCategory("Exercise");
            exercise1.setImageUrl("/images/health-habits/walking.jpg");
            exercise1.setOrder(1);
            healthHabitRepository.save(exercise1);
            
            HealthHabit exercise2 = new HealthHabit();
            exercise2.setTitle("Tai Chi for Health");
            exercise2.setContent("Tai Chi movements are slow and gentle, suitable for middle-aged and elderly people to practice. It can improve balance and relieve joint pain.");
            exercise2.setCategory("Exercise");
            exercise2.setImageUrl("/images/health-habits/taichi.jpg");
            exercise2.setOrder(2);
            healthHabitRepository.save(exercise2);
            
            // Sleep Category
            HealthHabit sleep1 = new HealthHabit();
            sleep1.setTitle("Regular Schedule");
            sleep1.setContent("Maintain a fixed sleep time. It is recommended to go to bed before 10 PM and ensure 7-8 hours of sleep. Avoid using electronic devices before bedtime.");
            sleep1.setCategory("Sleep");
            sleep1.setImageUrl("/images/health-habits/sleep.jpg");
            sleep1.setOrder(1);
            healthHabitRepository.save(sleep1);
            
            // Psychological Category
            HealthHabit mental1 = new HealthHabit();
            mental1.setTitle("Mindfulness Meditation");
            mental1.setContent("Practicing mindfulness meditation for 10-15 minutes every day can help relieve stress, improve mood, and enhance quality of life.");
            mental1.setCategory("Psychological");
            mental1.setImageUrl("/images/health-habits/meditation.jpg");
            mental1.setOrder(1);
            healthHabitRepository.save(mental1);
        }
        
        // Initialize nutrition diet data
        if (nutritionDietRepository.count() == 0) {
            // Breakfast Category
            NutritionDiet breakfast1 = new NutritionDiet();
            breakfast1.setTitle("Oatmeal");
            breakfast1.setContent("Oatmeal is rich in dietary fiber, which helps control blood sugar and cholesterol. It can be eaten with fruit and nuts.");
            breakfast1.setCategory("Breakfast");
            breakfast1.setImageUrl("/images/nutrition/oatmeal.jpg");
            breakfast1.setOrder(1);
            nutritionDietRepository.save(breakfast1);
            
            // Lunch Category
            NutritionDiet lunch1 = new NutritionDiet();
            lunch1.setTitle("Mediterranean Lunch");
            lunch1.setContent("Based on olive oil, fish, vegetables, and whole grains, rich in unsaturated fatty acids and antioxidants.");
            lunch1.setCategory("Lunch");
            lunch1.setImageUrl("/images/nutrition/mediterranean.jpg");
            lunch1.setOrder(1);
            nutritionDietRepository.save(lunch1);
            
            // Dinner Category
            NutritionDiet dinner1 = new NutritionDiet();
            dinner1.setTitle("Light Dinner");
            dinner1.setContent("The dinner should be light, mainly vegetables, with an appropriate amount of protein. Avoid greasy and overeating.");
            dinner1.setCategory("Dinner");
            dinner1.setImageUrl("/images/nutrition/light-dinner.jpg");
            dinner1.setOrder(1);
            nutritionDietRepository.save(dinner1);
            
            // Snacks Category
            NutritionDiet snack1 = new NutritionDiet();
            snack1.setTitle("Nut Snacks");
            snack1.setContent("Eat an appropriate amount of nuts, such as almonds and walnuts, which are rich in healthy fats and protein, but pay attention to controlling intake.");
            snack1.setCategory("Snacks");
            snack1.setImageUrl("/images/nutrition/nuts.jpg");
            snack1.setOrder(1);
            nutritionDietRepository.save(snack1);
        }
        
        // Initialize common medications for chronic diseases
        if (chronicDiseaseMedicationRepository.count() == 0) {
            // Hypertension Medication
            ChronicDiseaseMedication hypertension1 = new ChronicDiseaseMedication();
            hypertension1.setName("Amlodipine");
            hypertension1.setDisease("Hypertension");
            hypertension1.setUsage("Once daily, 5-10mg each time, taken after breakfast");
            hypertension1.setSideEffects("May cause headache, edema, palpitations, etc.");
            hypertension1.setPrecautions("Avoid taking with grapefruit juice, regularly monitor blood pressure");
            hypertension1.setImageUrl("/images/medications/amlodipine.jpg");
            hypertension1.setOrder(1);
            chronicDiseaseMedicationRepository.save(hypertension1);
            
            // Diabetes Medication
            ChronicDiseaseMedication diabetes1 = new ChronicDiseaseMedication();
            diabetes1.setName("Metformin");
            diabetes1.setDisease("Diabetes");
            diabetes1.setUsage("2-3 times daily, 500-850mg each time, taken after meals");
            diabetes1.setSideEffects("May cause gastrointestinal discomfort, such as nausea, diarrhea, etc.");
            diabetes1.setPrecautions("Use with caution in patients with renal insufficiency, regularly monitor blood sugar");
            diabetes1.setImageUrl("/images/medications/metformin.jpg");
            diabetes1.setOrder(1);
            chronicDiseaseMedicationRepository.save(diabetes1);
            
            // Heart Disease Medication
            ChronicDiseaseMedication coronary1 = new ChronicDiseaseMedication();
            coronary1.setName("Aspirin");
            coronary1.setDisease("Heart Disease");
            coronary1.setUsage("Once daily, 75-100mg each time, taken after meals");
            coronary1.setSideEffects("May cause gastrointestinal discomfort, increase bleeding risk");
            coronary1.setPrecautions("Use with caution in patients with bleeding tendencies, regularly check coagulation function");
            coronary1.setImageUrl("/images/medications/aspirin.jpg");
            coronary1.setOrder(1);
            chronicDiseaseMedicationRepository.save(coronary1);
        }

        // Initialize Doctor accounts
        if (!userRepository.existsByUsername("doctor")) {
            User doctorUser = new User();
            doctorUser.setUsername("doctor");
            doctorUser.setPassword(passwordEncoder.encode("doctor123"));
            doctorUser.setName("Dr. Sarah Smith");
            doctorUser.setEmail("sarah.smith@clinic.com");
            doctorUser.setPhone("1234567890");
            doctorUser.setActive(true);
            doctorUser.setCreatedAt(LocalDateTime.now());
            
            Set<User.Role> roles = new HashSet<>();
            roles.add(User.Role.ROLE_DOCTOR);
            doctorUser.setRoles(roles);
            
            User savedUser = userRepository.save(doctorUser);
            
            Doctor doctorProfile = new Doctor();
            doctorProfile.setUserId(savedUser.getId());
            doctorProfile.setName("Dr. Sarah Smith");
            doctorProfile.setDepartment("Endocrinology");
            doctorProfile.setSpecialty("Diabetes & Metabolism");
            doctorProfile.setTitle("Senior Consultant");
            doctorProfile.setHospitalName("City General Hospital");
            doctorProfile.setAvailable(true);
            doctorProfile.setCreatedAt(LocalDateTime.now());
            doctorRepository.save(doctorProfile);
        }

        if (!userRepository.existsByUsername("doctor2")) {
            User doctorUser2 = new User();
            doctorUser2.setUsername("doctor2");
            doctorUser2.setPassword(passwordEncoder.encode("doctor123"));
            doctorUser2.setName("Dr. James Wilson");
            doctorUser2.setEmail("james.wilson@clinic.com");
            doctorUser2.setPhone("0987654321");
            doctorUser2.setActive(true);
            doctorUser2.setCreatedAt(LocalDateTime.now());
            
            Set<User.Role> roles = new HashSet<>();
            roles.add(User.Role.ROLE_DOCTOR);
            doctorUser2.setRoles(roles);
            
            User savedUser2 = userRepository.save(doctorUser2);
            
            Doctor doctorProfile2 = new Doctor();
            doctorProfile2.setUserId(savedUser2.getId());
            doctorProfile2.setName("Dr. James Wilson");
            doctorProfile2.setDepartment("General Medicine");
            doctorProfile2.setSpecialty("Chronic Disease Management");
            doctorProfile2.setTitle("Chief Physician");
            doctorProfile2.setHospitalName("Metropolitan Health Center");
            doctorProfile2.setAvailable(true);
            doctorProfile2.setCreatedAt(LocalDateTime.now());
            doctorRepository.save(doctorProfile2);
        }

        // Initialize Admin account
        if (!userRepository.existsByUsername("admin")) {
            User adminUser = new User();
            adminUser.setUsername("admin");
            adminUser.setPassword(passwordEncoder.encode("admin123"));
            adminUser.setName("System Administrator");
            adminUser.setEmail("admin@diabetes-system.com");
            adminUser.setPhone("0000000000");
            adminUser.setActive(true);
            adminUser.setCreatedAt(LocalDateTime.now());
            
            Set<User.Role> roles = new HashSet<>();
            roles.add(User.Role.ROLE_ADMIN);
            adminUser.setRoles(roles);
            
            userRepository.save(adminUser);
        }

        // Initialize third Doctor account
        if (!userRepository.existsByUsername("doctor3")) {
            User doctorUser3 = new User();
            doctorUser3.setUsername("doctor3");
            doctorUser3.setPassword(passwordEncoder.encode("doctor123"));
            doctorUser3.setName("Dr. Nino");
            doctorUser3.setEmail("nino.doctor@clinic.com");
            doctorUser3.setPhone("1122334455");
            doctorUser3.setActive(true);
            doctorUser3.setCreatedAt(LocalDateTime.now());
            
            Set<User.Role> roles = new HashSet<>();
            roles.add(User.Role.ROLE_DOCTOR);
            doctorUser3.setRoles(roles);
            
            User savedUser3 = userRepository.save(doctorUser3);
            
            Doctor doctorProfile3 = new Doctor();
            doctorProfile3.setUserId(savedUser3.getId());
            doctorProfile3.setName("Dr. Nino");
            doctorProfile3.setDepartment("Internal Medicine");
            doctorProfile3.setSpecialty("Chronic Disease Prediction");
            doctorProfile3.setTitle("Associate Chief Physician");
            doctorProfile3.setHospitalName("University Hospital");
            doctorProfile3.setAvailable(true);
            doctorProfile3.setCreatedAt(LocalDateTime.now());
            doctorRepository.save(doctorProfile3);
        }
    }
}