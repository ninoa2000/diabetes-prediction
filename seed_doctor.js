/**
 * MongoDB Script to create a Doctor user and profile manually.
 * Usage: mongosh diabetes seed_doctor.js
 */

// 1. Create the User record
const doctorUser = {
  username: "doctor_nino",
  password: "$2a$10$8.UnVuG9HHgffUDAlk8qfOuVGkqRzgVymGe07xdVZz1SbgKGcp7uG", // doctor123
  name: "Dr. Nino",
  email: "nino.doctor@clinic.com",
  phone: "1122334455",
  roles: ["ROLE_DOCTOR"],
  active: true,
  createdAt: new Date(),
  updatedAt: new Date(),
  _class: "com.example.chronicdisease.model.User"
};

const userResult = db.users.insertOne(doctorUser);
const userId = userResult.insertedId.toString();

// 2. Create the Doctor profile record
const doctorProfile = {
  userId: userId,
  name: "Dr. Nino",
  department: "Internal Medicine",
  specialty: "Chronic Disease Prediction",
  title: "Associate Chief Physician",
  hospitalName: "University Hospital",
  available: true,
  patientIds: [],
  certificates: [],
  createdAt: new Date(),
  updatedAt: new Date(),
  _class: "com.example.chronicdisease.model.Doctor"
};

db.doctors.insertOne(doctorProfile);

print("Doctor 'doctor_nino' created successfully with password 'doctor123'");
