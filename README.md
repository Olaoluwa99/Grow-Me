# GrowMe App Submission

## App’s Functionality

The GrowMe app provides a seamless and intuitive shopping experience through well-structured screens and interactive features. From setting up the initial categories to managing a personalized shopping cart, the app ensures users can navigate effortlessly.

When the app is launched for the first time, it opens to the **Category Page**, where users set up their preferences. This setup process occurs only during the first run of the app. On subsequent visits, the Category Page displays available categories such as men’s clothing, women’s clothing, jewelry, and electronics. If the API provides more than four categories, the app dynamically creates a new category to accommodate the additional options, ensuring no category is left out.

From the Category Page, users can select their desired category, leading to the **Products Page**, which lists products belonging to the chosen category. Each product is displayed with its image, name, price, and rating, offering users clear and essential details to aid their purchase decisions. The Products Page also includes a "Buy" button for each item, allowing users to add products to their cart. Once added, users can specify the quantity of the product before proceeding to checkout.

A **Floating Action Button** (FAB) is present on both the Category and Products screens, providing quick access to the **Cart Page**. The Cart Page displays a detailed list of items added to the cart, with features to adjust product quantities or remove items entirely. This flexibility ensures users can easily manage their selections before finalizing their purchase. The Cart Page also includes a FAB for the checkout process, enabling users to complete their purchases conveniently.

The app is primarily optimized for **portrait mode on Android devices**, offering a tailored and user-friendly layout. However, it also supports other orientations, ensuring usability across various scenarios and preferences. This adaptability enhances the app's functionality for a broader range of devices and user habits.

## Design/Development Choices

- **Programming Language and Architecture**:  
  - **Kotlin & MVVM Architecture**: Kotlin ensures concise, safe, and modern code, while MVVM separates the user interface from business logic, making the app scalable and easier to maintain.

- **Dependency Injection**:  
  - **Hilt**: Simplifies the management of dependencies, reducing boilerplate code and improving the overall structure of the app.

- **UI Design**:  
  - **XML for UI Design**: Provides a structured and efficient way to define the app's layout, ensuring precise control over UI elements.  
  - **Dynamic Theming**: Customizes the app’s appearance based on the phone's wallpaper, creating a visually cohesive and personalized user experience.  
  - **Light and Dark Theming**: Adapts to user preferences and enhances usability in different lighting conditions.

- **Testing Approach**:  
  - **Test-Driven Development (TDD)**: Ensures reliability by allowing the app to be built and tested iteratively, reducing bugs and making it easier to maintain.

- **Image Loading**:  
  - **Glide**: Efficiently handles image loading and caching, reducing memory usage and ensuring fast rendering of images.

- **Data Persistence**:  
  - **Room Database**: Enables offline access to data and speeds up app performance by storing API data locally and synchronizing updates as needed.

- **Navigation**:  
  - **Navigation Component**: Ensures smooth and reliable transitions between fragments, reducing the complexity of navigation code.

- **Data Binding**:  
  - Links RecyclerView and UI components directly to the app's data, enabling seamless and reactive updates without additional overhead.

- **State Management**:  
  - **Flows**: Provides a streamlined way to monitor and react to state changes in the database, API, and ViewModel, ensuring a responsive and up-to-date UI.

## Setup Instructions

To set up the GrowMe app project on your local machine, follow these steps:

- **Clone the Repository**:  
  - Use the provided GitHub link to clone the repository onto your local system.  
    ```bash
    git clone https://github.com/Olaoluwa99/Grow-Me
    ```
  - Ensure you have Git installed on your system for this step.

- **Open the Project in Android Studio**:  
  - Launch Android Studio and open the cloned repository by navigating to its `build.gradle` file in the root directory.  
  - Allow Android Studio to index and sync the project files.

- **Download Dependencies**:  
  - Click on **Gradle Build** in the Android Studio toolbar. This process downloads and configures all necessary dependencies as specified in the `build.gradle` files.

- **Run the Project**:  
  - Once the dependencies are downloaded and the project is synced, click the **Run** button in Android Studio.  
  - Select a connected device or emulator to run the app and complete the setup process.

- **Additional Notes**:  
  - If prompted, update the **Android SDK** and other required tools from the Android Studio SDK Manager.  

Following these steps ensures a smooth setup process, enabling you to build, run, and modify the GrowMe app effectively.
