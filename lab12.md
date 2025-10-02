![CmuLogo](https://web.archive.org/web/20251002092854im_/https://lh7-rt.googleusercontent.com/docsd/ANYlcfCGFVuKMpd40dY5nWEzrIQ9vXPlaCMRzwBVGPEoulYlPqUUv_nm4a0ERoxKcsIYjPGfCcO-5GHIxOpHboa8zv803Ok6c3O_KFu_btaZQkJDfGXRRwy8-QPXKO-eQ_9JjQKrh_U2k5YOSlnpWUI=s800)

CHIANG MAI UNIVERSITY

Bachelor of Science (Software Engineering)

College of Arts, Media and Technology

1st Semester / Academic Year 2025

SE 331 Component Based Software Development

![](https://web.archive.org/web/20251002092854im_/https://docs.google.com/drawings/d/1VDBEXxoh_wq9a6aIRrrmlGdhENZbvB1TwPpXZjoFGDPDIoBcHz4UHKWRNlLfykTJ7KGD_7BNi-Bo4uIABfJLYWAdsJBFRi8KFiLtCEAbxDcQaRZk/image?parent=1NgoEXWbnrNxcYaKh685cReiyBNrBbJYnsV8ZqijNtno&amp;rev=1&amp;drawingRevisionAccessToken=dCfzPJCgJnEDlA&amp;h=9&amp;w=9&amp;ac=1)

Spring Security

Name …………………..……………. ID ……………………

_Objective_ In this session, you will learn how to apply the Spring security to the application.

_Hint _ The symbol + and – in front of the source code is to show that you have to remove the source code and add the source code only. There are not the part of the source code

1. Setting up the Spring Security modules

1.1. Setting up the Spring security by updating the pom.xml to use the Spring-security and related component

```
         </dependency>
       <!-- https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-starter-security -->
       <dependency>
           <groupId>org.springframework.boot</groupId>
           <artifactId>spring-boot-starter-security</artifactId>
       </dependency>
         <!-- https://mvnrepository.com/artifact/io.jsonwebtoken/jjwt-api -->
         <dependency>
                <groupId>io.jsonwebtoken</groupId>
           <artifactId>jjwt-api</artifactId>
           <version>0.12.6</version>
       </dependency>
           <!-- https://mvnrepository.com/artifact/io.jsonwebtoken/jjwt-impl -->
         <dependency>
                <groupId>io.jsonwebtoken</groupId>
                <artifactId>jjwt-impl</artifactId>
                <version>0.12.6</version>
                <scope>runtime</scope>
         </dependency>
         <!-- https://mvnrepository.com/artifact/io.jsonwebtoken/jjwt-orgjson -->
         <dependency>
                <groupId>io.jsonwebtoken</groupId>
                <artifactId>jjwt-jackson</artifactId>
                <version>0.12.6</version>
                <scope>runtime</scope>
         </dependency>
        </dependencies>
```

1.2. Copy the file Securiy-module.zip, unzip to the project in src/main/java/se331.lab.rest

Note  you may have to do this in the file explorer or finder

```
show the list of  packages and class names which you just copy
```

1.3. Update the application.yml to setup the JWT

```
application:
security:
  jwt:
    secret-key: [your secret]
    expiration: 86400000 # 86400000 # a day
    refresh-token:
      expiration: 604800000 # 7 days
```

your secret is the random secret key whose length is 512 bits (to match the HS512 Algorithm). You may generate the secret randomly from this _[website](https://web.archive.org/web/20251002092854/https://www.google.com/url?q=https://jwtsecrets.com/&sa=D&source=editors&ust=1759400934585074&usg=AOvVaw1T1SuWk1F60lA2zJTa6ngx)_.

![A screenshot of a computer

Description automatically generated](https://web.archive.org/web/20251002092854im_/https://lh7-rt.googleusercontent.com/docsd/ANYlcfD38UNCk4HgKaT70A0Cjis6o-DJGUtYVVhPvvEjHCz5uwHrT1aaJheOiyo-6q7xIqGGpF-R0joishTgL0M94pVrvqATi4pCVcTjMykbfpzeVK1SnvxWf2VYTP2BNjVSwNdGbiLM26_jeSvAIvA=s800)

```
        then copy the value to your secret
```

1.4. Now restart the backend server, open the frontend,  you should try to use your front-end application to see the data, you should not see it. Look at the developer console on the Network page, what is the return HTTP code for the request for data?
1.5. Use the Rest client to call the events in the backend server, what do they return?

2. Now we will use the JWT (Java Web Token) to authorize who can access the REST Resource, the User class which will handle the username, and password is already in the Spring module, the roles is provided in the Roles Enumeration. All provided repository is in the package security, you can browse on it

2.1. Add the authority user to mange the role.

2.2. Add the User object, which will be used to authorize in the InitApp class,  and create a new method to add the user.

```
     final EventRepository eventRepository;
     final OrganizerRepository organizerRepository;
   final UserRepository userRepository;
```

```
     @Override
     @Transactional
```

...

```
         tempEvent.setOrganizer(org3);
         org3.getOwnEvents().add(tempEvent);
       addUser();
   }
   User user1,user2,user3;
   private void addUser() {
               PasswordEncoder encoder = new BCryptPasswordEncoder();
               user1 = User.builder()
                               .username("admin")
                               .password(encoder.encode("admin"))
                               .firstname("admin")
                               .lastname("admin")
                               .email("admin@admin.com")
                               .enabled(true)
                               .build();
                       user2 = User.builder()
                               .username("user")
                               .password(encoder.encode("user"))
                               .firstname("user")
                               .lastname("user")
                               .email("enabled@user.com")
                               .enabled(true)
                               .build();
               user3 = User.builder()
                               .username("disableUser")
                               .password(encoder.encode("disableUser"))
                               .firstname("disableUser")
                               .lastname("disableUser")
                               .email("disableUser@user.com")
                               .enabled(false)
                               .build();
               user1.getRoles().add(Role.ROLE_USER);
               user1.getRoles().add(Role.ROLE_ADMIN);
               user2.getRoles().add(Role.ROLE_USER);
               user3.getRoles().add(Role.ROLE_USER);
               userRepository.save(user1);
               userRepository.save(user2);
               userRepository.save(user3);
     }
```

2.3. Restart your backend application, Open the ApiDog, using the setting as given

![](https://web.archive.org/web/20251002092854im_/https://lh7-rt.googleusercontent.com/docsd/ANYlcfCey5JcELIs9nZiBfRtdmiN9k9oeunS9gr8oasrl-ZFEKIq9WWU5CCseKo-kKW84xeeF0xzC1q0xSp_d2S2oTAwzNJfZq9qAEH0MQCwrfpQPkcYjxAlj7wzFYEU7gpAnApzweOW_Yn4z8ji=s800)

2.3.
   ```
   1 Show the staff what is the return value, and show the staff which part of source code which handle this request (The controller class)
   ```

2.3. 2 Open the database, show how does the password stored in the database

However, you still did not get any data, update the se331.lab.rest.security.config.SecurityConfiguration to allow the authentication

http.csrf((crsf) -> crsf.disable())

```
    .authorizeHttpRequests((authorize) -> {
       authorize.requestMatchers("/api/v1/auth/**").permitAll()
        .anyRequest().authenticated();
     })
```

2.4. Run the api call again to see the return values
2.5. get back to the query page, try to call the query all event again what is the result?
2.6. copy the token you receive and then select the header and add your token as shown in the image below

![A screenshot of a computer

Description automatically generated](https://web.archive.org/web/20251002092854im_/https://lh7-rt.googleusercontent.com/docsd/ANYlcfBbcGpesJy4zkv_oyutwhalkTqq_k-EAMh0PKY4WGguzGWPCaBgB0dfNwhyyswEGjIznziTt_NZnHJ8zl2PWhftjrHU_iNRr1lxYWOkj3MaEChQd5xQSWO4LRyykBj7_KgwvgsHul3BDu5f=s800)

```
        show the result of the query
```

2.7. Now, as we have used the spring security, the security may block the http OPTIONS methods because it will catch the request in the JwtAuthenticationFilter, so update the doFilterInternal as  given

```
 protected void doFilterInternal(
     @NonNull HttpServletRequest request,
     @NonNull HttpServletResponse response,
     @NonNull FilterChain filterChain
 ) throws ServletException, IOException {
//     Always let preflight through
   if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
    filterChain.doFilter(request, response);
    return;
   }
   if (request.getServletPath().contains("/api/v1/auth")) {
     filterChain.doFilter(request, response);
     return;
   }
```

2.8.
   ```
   the cors setting now should be set in the security not the app, so open the Application class and remove the methods corsConfigurer
   ```

```
@Bean
public WebMvcConfigurer corsConfigurer() {
   return new WebMvcConfigurer() {
       @Override
       public void addCorsMappings(CorsRegistry registry) {
```

```
           registry.addMapping("/**")
               .allowedOrigins("http://localhost:5173","http://47.129.170.143:8001")
               .exposedHeaders("x-total-count");
       }
   };
}
```

2.9. Open SecurityConfiguration.java, add the bean to create the cors setting

```
@Bean
public CorsConfigurationSource corsConfigurationSource() {
   CorsConfiguration config = new CorsConfiguration();
```

```
   config.setAllowedOriginPatterns(List.of("http://localhost:5173",                 "http://13.212.6.216:8001"));
```

```
   config.setAllowedMethods(List.of("GET","POST","PUT","DELETE","OPTIONS"));
   config.setAllowedHeaders(List.of("*"));
   config.setExposedHeaders(List.of("x-total-count"));  
    config.setAllowCredentials(true);
```

```
   UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
   source.registerCorsConfiguration("/**", config);
   return source;
}
```

2.10. add the security chain to add the source to the securityFilterChain

```
@Bean
public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
```

```
   http.headers((headers) -> {
       headers.frameOptions((frameOptions) -> frameOptions.disable());
   });
   http
           .cors(cors -> cors.configurationSource(corsConfigurationSource()))
           .csrf((crsf) -> crsf.disable())
           .authorizeHttpRequests((authorize) -> {
```

2.11. To confirm that the security filter will be used before the JwtAuthenticationFilter, add the cors filter with the highest precedent in the SecurityConfiguration

```
@Bean
public FilterRegistrationBean<CorsFilter> corsFilterBean() {
   FilterRegistrationBean<CorsFilter> bean =
           new FilterRegistrationBean<>(new CorsFilter(corsConfigurationSource()));
   bean.setOrder(Ordered.HIGHEST_PRECEDENCE); // run before security/JWT
   return bean;
}
```

3. Now we will create the login form with the validation to login from our system.

3.1. To create the login view, we need to install the tailwinds plugin. Forms. to install the plugin using the given command

npm install -D @tailwindcss/forms

```
        then update the tailwind.config.js to install the plugins as given
```

plugins: [],

plugins: [

```
   require('@tailwindcss/forms')
```

],

3.2. Create the login form by creating the LoginView.vue in the views folder using the given information

```
<template>
   <div class="flex min-h-full flex-1 flex-col justify-center px-6 py-12 lg:px-8">
     <div class="sm:mx-auto sm:w-full sm:max-w-sm">
       <img class="mx-auto h-10 w-auto" src="https://tailwindui.com/img/logos/mark.svg?color=indigo&shade=600" alt="Your Company" />
       <h2 class="mt-10 text-center text-2xl font-bold leading-9 tracking-tight text-gray-900">Sign in to your account</h2>
     </div>
     <div class="mt-10 sm:mx-auto sm:w-full sm:max-w-sm">
       <form class="space-y-6" action="#" method="POST">
         <div>
           <label for="email" class="block text-sm font-medium leading-6 text-gray-900">Email address</label>
           <div class="mt-2">
             <input id="email" name="email" type="email" required="" class="block w-full rounded-md border-0 py-1.5 text-gray-900 shadow-sm ring-1 ring-inset ring-gray-300 placeholder:text-gray-400 focus:ring-2 focus:ring-inset focus:ring-indigo-600 sm:text-sm sm:leading-6" />
           </div>
         </div>
         <div>
           <div class="flex items-center justify-between">
             <label for="password" class="block text-sm font-medium leading-6 text-gray-900">Password</label>
             <div class="text-sm">
               <a href="#" class="font-semibold text-indigo-600 hover:text-indigo-500">Forgot password?</a>
             </div>
           </div>
           <div class="mt-2">
             <input id="password" name="password" type="password" class="block w-full rounded-md border-0 py-1.5 text-gray-900 shadow-sm ring-1 ring-inset ring-gray-300 placeholder:text-gray-400 focus:ring-2 focus:ring-inset focus:ring-indigo-600 sm:text-sm sm:leading-6" />
           </div>
         </div>
         <div>
           <button type="submit" class="flex w-full justify-center rounded-md bg-indigo-600 px-3 py-1.5 text-sm font-semibold leading-6 text-white shadow-sm hover:bg-indigo-500 focus-visible:outline focus-visible:outline-2 focus-visible:outline-offset-2 focus-visible:outline-indigo-600">Sign in</button>
         </div>
       </form>
       <p class="mt-10 text-center text-sm text-gray-500">
         Not a member?
         {{ ' ' }}
         <a href="#" class="font-semibold leading-6 text-indigo-600 hover:text-indigo-500">Try to register here</a>
       </p>
     </div>
   </div>
 </template>
```

3.3. add the router to the new login page in the src/rouer/index.ts

},

```
   {
     path: '/login',
     name: 'login',
     component: LoginView
   },
   {
     path: '/event/:id',
```

3.4. Install the vue-validate, and yup to validate the input by calling the command in the terminal

npm install vee-validate, yup

3.5. Create the input component that contains the error to be shown. Creating src/components/ErrorMessage.vue to show the error message

```
<script setup lang="ts">
interface ErrorMessageProps {
  id: string
}
```

defineProps()

```
</script>
<template>
  <p aria-live="assertive" class="errorMessage" :id="id">
    <slot />
  </p>
</template>
```

create src/features/UniqueID.ts to generate the unique id for the components

```
let UUID = 0
export default function UniqueID() {
 const getID = (): string => {
   UUID++
   return String(UUID)
 }
 return {
   getID
 }
}
```

create the src/components/InputText.vue to hold the input text box, which packed with the error message

```
<script setup lang="ts">
import UniqueID from '@/features/UniqueID'
import ErrorMessage from '@/components/ErrorMessage.vue'
import { computed } from 'vue'
const modelValue = defineModel()
interface Props { 
placeholder?: string 
error?: string
required?: boolean
type: string
}
const props = withDefaults(defineProps<Props>(), {
placeholder: '', 
error: '',
required: false,
type: 'text'
})
const uuid = UniqueID().getID()
const placeholderErrorClass = computed(() => {
return !isError.value
```

```
  ? 'block w-full rounded-md border-0 py-1.5 text-gray-900 shadow-sm ring-1 ring-inset ring-gray-300 placeholder:text-gray-400 focus:ring-2 focus:ring-inset focus:ring-indigo-600 sm:text-sm sm:leading-6'     
```

```
  : 'block w-full rounded-md border-0 py-1.5 text-red-700 shadow-sm ring-1 ring-inset ring-red-300 placeholder:text-red-400 focus:ring-2 focus:ring-inset focus:ring-red-600 sm:text-sm sm:leading-6'
```

```
})
const isError = computed(() => {
return props.error ? true : false
})
</script>
<template>
<div>   
  <div class="mt-2">
    <input
      :type="type"
      :id="uuid"
      :class="placeholderErrorClass"
      :placeholder="placeholder"       
      v-bind=""
      v-model="modelValue"
      :aria-describedby="error ? `${uuid}-error` : undefined"
      :aria-invalid="error ? true : false"
    />
    <ErrorMessage class="inline-flex text-sm text-red-700" v-if="error" :id="`${uuid}-error`">
      {{ error }}
      <svg
        xmlns="http://www.w3.org/2000/svg"
        class="w-6 h-6"
        fill="none"
        viewBox="0 0 24 24"
        stroke="currentColor"
      >
        <path
          stroke-linecap="round"
          stroke-linejoin="round"
          stroke-width="2"
          d="M6 18L18 6M6 6l12 12"
        />
      </svg>
    </ErrorMessage>
  </div>
</div>
</template>
```

then update the LoginView.vue to use the new component by removing the old input with the new component as given

add the script part of the code

```
<script setup lang="ts">
import InputText from '@/components/InputText.vue'
import { ref } from 'vue'
const email = ref('')
const password = ref('')
</script>
```

update the form view

```
       <form class="space-y-6" action="#" method="POST">
         <div>
           <label for="email" class="block text-sm font-medium leading-6 text-gray-900">Email address</label>
           <div class="mt-2">
             <input id="email" name="email" type="email" required="" class="block w-full rounded-md border-0 py-1.5 text-gray-900 shadow-sm ring-1 ring-inset ring-gray-300 placeholder:text-gray-400 focus:ring-2 focus:ring-inset focus:ring-indigo-600 sm:text-sm sm:leading-6" />
           </div>
         <InputText type="email" v-model="email" placeholder="Email address" />
         </div>
         <div>
           <div class="flex items-center justify-between">
             <label for="password" class="block text-sm font-medium leading-6 text-gray-900">Password</label>
             <div class="text-sm">
               <a href="#" class="font-semibold text-indigo-600 hover:text-indigo-500">Forgot password?</a>
             </div>
           </div>
           <div class="mt-2">
             <input id="password" name="password" type="password" class="block w-full rounded-md border-0 py-1.5 text-gray-900 shadow-sm ring-1 ring-inset ring-gray-300 placeholder:text-gray-400 focus:ring-2 focus:ring-inset focus:ring-indigo-600 sm:text-sm sm:leading-6" />
```

```
         <InputText type="email" v-model="email" placeholder="Email address" />
         </div>
```

3.6. now we can setup the validation to our LoginView.vue

```
<script setup lang="ts">
import InputText from '@/components/InputText.vue';
import { ref } from 'vue'
const email = ref('')
const password = ref('')
import * as yup from 'yup'
import { useField, useForm } from 'vee-validate'
const validationSchema = yup.object({
 email: yup.string().required('The email is required').email('Input must be an email.'),
```

```
 password: yup.string().required('The password is required').min(6,'The password must be at least 6 characters.')
```

})

```
const { errors, handleSubmit } = useForm({
```

validationSchema,

```
 initialValues: {
   email: '',
   password: ''
 }
```

})

```
const { value: email } = useField<string>('email')
const { value: password } = useField<string>('password')
const => {
```

console.log(values)

})

```
</script>
```

…

```
   <div class="mt-10 sm:mx-auto sm:w-full sm:max-w-sm">
     <form class="space-y-6" action="#" method="POST">
     <form class="space-y-6" @submit.prevent="onSubmit">
       <div>
```

…

```
         <InputText type="email" v-model="email" placeholder="Email address"></InputText>
         <InputText type="email" v-model="email" placeholder="Email address" :error="errors['email']"></InputText>
       </div>
```

…

```
         </div>
         <InputText v-model="password" type="password" placeholder="Password"></InputText>
         <InputText v-model="password" type="password" placeholder="Password" :error="errors['password']" ></InputText>
```

3.7. then try to show the errors which can be show in the login page
3.8. Try to enter the valid username, and password and show the results on the console to the staff

4. Create the Login mechanism.

4.1. The login service can be set in the Pinia to keep the single source of Truth to our application, create the auth store in src/stores/auth.ts

```
import { defineStore } from 'pinia'
import axios from 'axios'
import type { AxiosInstance } from 'axios'
const apiClient: AxiosInstance = axios.create({
 baseURL: import.meta.env.VITE_BACKEND_URL,
```

withCredentials: false,

```
 headers: {
   Accept: 'application/json',
   'Content-Type': 'application/json'
 }
```

})

```
export const useAuthStore = defineStore('auth', {
 state: () => ({
   token: null as string | null
```

}),

```
 actions: {
   login(email: string, password: string) {
     return apiClient
       .post('/api/v1/auth/authenticate', {
         username: email,
         password: password
       })
       .then((response) => {         
         this.token = response.data.access_token
         return response
       })
   }
 }
```

})

update the LoginView.vue to use the new AuthStore

```
import { useAuthStore } from '@/stores/auth'
const authStore = useAuthStore()
```

…

```
const => {
```

console.log(values)

```
const => { 
```

authStore.login(values.email, values.password)

})

Then try to use the valid user name to login to the system

4.2. Oops, our valid username and password do not conform to our validation. Now we should remove some validation for the study purpose only, update the LoginView.vue

```
const validationSchema = yup.object({
 email: yup.string().required('The email is required').email('Input must be an email.'),
```

```
 password: yup.string().required('The password is required').min(6,'The password must be at least 6 characters.')
```

```
 email: yup.string().required('The email is required'),
 password: yup.string().required('The password is required')
```

})

…

```
         <InputText type="email" v-model="email" :error="errors['email']"></InputText>
         <InputText type="text" v-model="email" :error="errors['email']"></InputText>
       </div>
```

then try to login again. There may be some errors in the console. Please check

4.3. Now you should be able to add login, try to login and show the token in the Pinia store using VueDevTools
4.4. add the callback to log that you have successfully login, update the LoginView as given

```
const => { 
```

authStore.login(values.email, values.password)

```
 .then(() => {
   console.log('login success')
```

})

})

```
</script>
```

4.5. Try to add the error with the given code

authStore.login(values.email, values.password)

```
 .then(() => {
   console.log('login success')
 }).catch((err) => {
   console.log('error',err)
```

})

_Then try to show the console result when you login with the valid and invalid username and password_

5. Try to open the Home page, we cannot see any events anymore even we have login to the system. If you remember, if we want to get the data from the server we need to add the Authorization header. we can inject the header in each call, but that will take a lot of work to do. We can use the interceptor to add the Authorization header in every axios call.

```
        Refactor the services class to extract the apiClient to be called.
```

Create the AxiosClient.ts in the services folder to create the single Axios object

```
import axios from 'axios'
const apiClient = axios.create({
 baseURL: import.meta.env.VITE_BACKEND_URL,
 headers: {
   Accept: 'application/json',
   'Content-Type': 'application/json'
 }
})
export default apiClient
```

update the EventServices.ts, and OrganizerService.ts to use the new apiClient

```
import axios from 'axios'
import apiClient from './AxiosClient'
const apiClient: AxiosInstance = axios.create({
 baseURL: import.meta.env.VITE_BACKEND_URL,
```

withCredentials: false,

```
 headers: {
   Accept: 'application/json',
   'Content-Type': 'application/json'
 }
```

})

5.6. Add the interceptor by create the AxiosInterceptorSetup.ts in the services folder

```
import apiClient from '@/services/AxiosClient.js'
```

apiClient.interceptors.request.use(

```
 (request) => {   
   const token = localStorage.getItem('access_token')
   console.log('token', token)
   if (token) {
     request.headers['Authorization'] = 'Bearer ' + token
   }
   return request
```

},

```
 (error) => {
   return Promise.reject(error)
 }
```

)

5.7. then update the main.ts to call the inceptor setup

```
import 'nprogress/nprogress.css'
import '@/services/AxiosInrceptorSetup.ts'
const app = createApp(App)
```

as the token is read from the local storage, update auth.ts to store the token in the local storage.

```
       .then((response) => {         
         this.token = response.data.access_token
         localStorage.setItem('access_token', this.token as string)
         axios.defaults.headers.common['Authorization'] = `Bearer ${this.token}`
         return response
       }) 
```

now you should try to login then click the Home menu. You should see the list of events now

_Note_ that you may try to open the  development console, open the application. if you have already login you should see the token as shown below

![A screenshot of a computer

Description automatically generated](https://web.archive.org/web/20251002092854im_/https://lh7-rt.googleusercontent.com/docsd/ANYlcfCxDOmzdqMSmUmvpo4dYgDaUuWkm9PGxMAKOXlRVAO_IUgEuQKig_EfYBeqPuLcklwmVb9eDSzVsHvPf0BndWqiDojVC_v8LSa9YJOlgBArnoMjEMYz8yyrG9vSFdDHWJ2_hglXLuXSOBG17hk=s800)

if you want to logout, you can delete the token in the Application tab directly. try to remove the token, and then refresh the web page. The event list should not be shown. In addition please see what happen in the auth store via Vue Dev Tools

5.8. Setting that after login, the homepage (event-list) will be shown, update the LoginView.vue to move to the default page if the login is correct

```
import { useRouter } from 'vue-router'
const router = useRouter()
const => { 
```

authStore.login(values.email, values.password)

```
 .then(() => {
   console.log('login success')
   router.push({ name: 'event-list-view' })
 }).catch((err) => {
```

_ staff that the user is moved to the homepage if the correct username, and password is provided_

5.9. To handle when the user puts the wrong username, or password. Update the LoginView.vue by adding the store message.

```
import { useMessageStore } from '@/stores/message';
const messageStore = useMessageStore()
```

authStore.login(values.email, values.password)

```
 .then(() => {
   router.push({ name: 'event-list' })
 }).catch((err) => {
   console.log('error',err)
```

})

```
   .then(() => {
     router.push({ name: 'event-list' })
   }).catch(() => {
     messageStore.updateMessage('could not login')
     setTimeout(() => {
       messageStore.resetMessage()
     }, 3000)
   })
```

_you may try login with the wrong username, and password _

6. After the login has been implemented, now we should make a menu bar, so that it is easier for the user to login to the system.

6.1. We can use the Material Design Icon to make our application look more friendly. Install the material Design Icon using this instruction Getting Started with Vue.js - Docs - Pictogrammers .
6.2. Update the App.vue to add the nav bar

```
   <header class="max-h-screen leading-normal">
       <div id="flashMessage" v-if="message">
           <h4>{{ message }}</h4>
       </div>
       <div class="wrapper">
        <nav class="py-6">
     <nav class="flex">
               <ul class="flex navbar-nav ml-auto">
                   <li class="nav-item px-2">
                       <router-link to="/register" class="nav-link">
                        <div class="flex items-center">
                            <SvgIcon type="mdi" :path="mdiAccountPlus"/> <span class="ml-3">Sign Up</span>
                        </div>
```

```
                       </router-link>
                   </li>
                   <li class="nav-item px-2">
                       <router-link to="/login" class="nav-link">
                        <div class="flex items-center">
                            <SvgIcon type="mdi" :path="mdiLogin"/> <span class="ml-3">Login</span>
                        </div>                           
                       </router-link>
                   </li>
               </ul>
           </nav>           
    <RouterLink
            class="font-bold text-gray-700"
            exact-active-class="text-green-500"
            :to="{ name: 'event-list-view' }"
            >Event</RouterLink
          >
          |
          <RouterLink 
```

Note you can see the example of using the icon here _[login - Material Design Icons - Pictogrammers](https://web.archive.org/web/20251002092854/https://www.google.com/url?q=https://pictogrammers.com/library/mdi/icon/login/&sa=D&source=editors&ust=1759400934683869&usg=AOvVaw1-4I9-3QkBJLitHl75iV7u)_.

6.3.
   ```
   We assume that after login, the name of the user should be shown. we need to send the data from the backend about the details of the user who has logged in. Get back to the backend, send the user information by updating the AuthenticationController class. Add the linkage from the User class (for authentication)  to the organizer which will be login
   ```
6.4. Update the Organizer.java to link with the User

```
    List<Event> ownEvents = new ArrayList<>();
   @OneToOne
   User user;
}        
```

6.5. Update the User.java to link with the organizer

```
    @ManyToMany(fetch = FetchType.EAGER)
    private List<Authority> authorities = new ArrayList<>();
    @OneToOne(mappedBy = "user")
   Organizer organizer;
}
```

6.6. Link the User, and Organizer in the InitApp.java

```
        addUser();
       org1.setUser(user1);
       user1.setOrganizer(org1);
       org2.setUser(user2);
       user2.setOrganizer(org2);
       org3.setUser(user3);
       user3.setOrganizer(org3);
    }
```

6.7. Then Update the AuthenticationRespose.java to return the Organizer object which will be shown when we have login.

```
 private String refreshToken;
 private Organizer user;
}
```

6.8. Update the AuthenticationService.java in the authenticate method to return the user.

```
   return AuthenticationResponse.builder()
           .accessToken(jwtToken)
           .refreshToken(refreshToken)
           .user(user.getOrganizer())
           .build();
 }
        Try to login in the Postman again. see any problem.
```

6.9. Again, the stack overflow problem occurred again. so Using the OrgainzerDTO to send the data. update the AuthenticationResponse.java as given below

```
 private Organizer user;
 private OrganizerDTO user;
}
```

then update the authenticate methods in AuthenticationService.java

```
   return AuthenticationResponse.builder()
           .accessToken(jwtToken)
           .refreshToken(refreshToken)
           .user(user.getOrganizer())
           .user(LabMapper.INSTANCE.getOrganizerDTO(user.getOrganizer()))
           .build();
 }
```

now try to login in the APIDog again,

6.10. After finished sending the data, getting back to front end, update the auth.ts to store the user information in the in the state.

```
import type { Organizer } from '@/types'
export const useAuthStore = defineStore('auth', {
 state: () => ({
   token: null as string | null
   token: null as string | null,
   user: null as Organizer | null
```

}),

```
 getters: {
   currentUserName(): string {
     return this.user?.name || ''
   }
```

},

```
 actions: {
   login(email: string, password: string) {
     return apiClient
       .post('/api/v1/auth/authenticate', {
         username: email,
         password: password
       })
       .then((response) => {
         this.token = response.data.access_token
         this.user = response.data.user
         localStorage.setItem('access_token', this.token as string)
         localStorage.setItem('user', JSON.stringify(this.user))
         return response
           })
```

6.11. Update the App.vue, to be able to change the menu bar when we click login, add the computed property which will be used when rendering the page.

```
<script setup lang="ts">
import { RouterLink, RouterView } from 'vue-router'
import { useMessageStore } from '@/stores/message'
import { useAuthStore } from './stores/auth';
import { storeToRefs } from 'pinia'
import { useRouter } from 'vue-router';
const store = useMessageStore()
const authStore = useAuthStore()
const router = useRouter()
const { message } = storeToRefs(store)
import { mdiAccount } from '@mdi/js'
import { mdiLogout } from '@mdi/js'
</script>
```

…

```
               <ul class="flex navbar-nav ml-auto">
               <ul v-if="!authStore.currentUserName" class="flex navbar-nav ml-auto">
                   <li class="nav-item px-2">
```

…

```
               </ul>
            <ul v-if="authStore.currentUserName" class="flex navbar-nav ml-auto">
              <li class="nav-item px-2">
                <router-link to="/profile" class="nav-link">
                  <div class="flex items-center">
                    <SvgIcon type="mdi" :path="mdiAccount" />
                    <span class="ml-3">{{ authStore.currentUserName }}</span>
                  </div>
                </router-link>
              </li>
              <li class="nav-item px-2">
                <a class="nav-link hover:cursor-pointer" @click="logout">
                  <div class="flex items-center">
                    <SvgIcon type="mdi" :path="mdiLogin" /> <span class="ml-3"> LogOut</span>
                  </div>
                </a>
              </li>
            </ul>
           </nav>
           <RouterLink to="/">Home</RouterLink> |
```

6.12.
   ```
   The logout function is used in the App.vue. So add the logout function in App.vue
   ```

```
function logout() {
   authStore.logout()
   router.push({ name: 'login' })
}
```

```
        and then add logout action in the action of auth store
   logout() {
     console.log('logout')
     this.token = null
     this.user = null
     localStorage.removeItem('access_token')
     localStorage.removeItem('user')
   },
```

6.13. However, if you login, and reload the home page, you may notice that the menu bar does not show the user name. it is because the state  is always reloaded (set as null). We can change this behaviour by loading the data from the localStorage if there are. so update the App.vue to reload the token, and user if provided

```
const token = localStorage.getItem('access_token')
const user = localStorage.getItem('user')
if (token && user) {
   authStore.reload(token,JSON.parse(user))   
}else{
   authStore.logout()
}
```

and then update the auth.ts to be able to reload by adding the reload action

```
   },
   reload(token: string, user: EventOrganizer) {
     this.token = token
     this.user = user
   }
```

_Now show the front end to login or logout of the system, and when login when you click reload the username is still shown_

6.14. To allow the upload file to be able to use via the security we have set, update the auth.ts to provide the Authorization value from the pinia

```
 getters: {
   currentUserName(): string {
     return this.user?.name || ''
   },
   isAdmin(): boolean {
     return this.user?.roles.includes('ROLE_ADMIN') || false
   },
  authorizationHeader(): string {
    return `Bearer ${this.token}`
  }
```

},

6.15. Open the ImageUpload component, Provide the computed property to read the data from the pinia and provide the header object to be inserted. In addition, do not forget to import authStore to the component.

```
const authorizeHeader = computed(() => {
return { authorization: authStore.authorizationHeader }
})
```

6.16. Add the headers to the Upload component.

```
<template>
```

<Uploader

```
   :server="uploadUrl"
   @change="onChanged"
   :media="media"
  :headers="authorizeHeader"
 ></Uploader>
</template>
```

7. Some time we want to allow some paths using some http methods to be able to be accessed by everybody. For example, we should allow everybody to see the list of events even if they have not logged in.

7.1. get back to your backend update the SecurityConfiguration.java to allow the GET method of the path /events

```
         http.csrf((crsf) -> crsf.disable())
            .authorizeHttpRequests((authorize) -> {
                authorize.requestMatchers("/api/v1/auth/**").permitAll()
                        .requestMatchers(HttpMethod.GET,"/events").permitAll()
                        .requestMatchers(HttpMethod.OPTIONS,"/**").permitAll()
                        .anyRequest().authenticated();
            })
```

Now try to go to the  homepage without logging in. You should be able to see the list of events even you have not logged in.

7.2. Open Add Event page, you may notice that the organizer list is not shown, Fix the source code which will help the users to see the list of organizer

8. All types of users should not be able to see all the menu. For example only the admin should be able to add a new event. Now we need to know the Authority of each user

8.1. Create the OrganizerAuthDTO.java to handle the DTO of the organizer object as the user data

```
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrganizerAuthDTO {
   Long id;
   String name;
   List<Role> roles = new ArrayList<>();
}
```

8.2. Add a new Method for the LabMapper to get the OrganizerAuthDTO

```
    OrganizerDTO getOrganizerDTO(Organizer organizer);
    List<OrganizerDTO> getOrganizerDTO(List<Organizer> organizers);
   @Mapping(target = "roles", source = "user.roles")
   OrganizerAuthDTO getOrganizerAuthDTO(Organizer organizer);
}
```

8.3. Update the AuthenticationResponse.java to return OrganizerAuthDTO

```
           private OrganizerDTO user;
           private OrganizerAuthDTO user;
```

8.4. Update the AuthenticationService to change the return types.

```
           .user(LabMapper.INSTANCE.getOrganizerDTO(user.getOrganizer()))
           .user(LabMapper.INSTANCE.getOrganizerAuthDTO(user.getOrganizer()))
        .build()
```

8.5. Try to login in the ApiDog to see the result
8.6.
   ```
   Get back to the frontend, now when the user logs in, they will have the authorities as the role of the user (you can see the role in the developer console in the Application tab), now we need a function to check for the user’s role.
   ```

add the roles to the Organizer type

```
export interface Organizer{
```

id: number

```
 name: string
 roles: string[]
}
```

add the isAdmin getter to the auth.ts to check that the current user is admin or not.

```
   currentUserName(): string {
     return this.user?.name || ''
   },
   isAdmin(): boolean {
     return this.user?.roles.includes('ROLE_ADMIN') || false
   }
```

},

8.7. then update the App.vue to hide the New Event menu if the current user is not the admin

```
           <RouterLink to="/">Home</RouterLink> |
           <RouterLink to="/about">About</RouterLink> |
           <RouterLink :to="{ name: 'add-event' }">New Event</RouterLink>
           <RouterLink to="/about">About</RouterLink>
           <span v-if="authStore.isAdmin"> |
               <RouterLink :to="{ name: 'add-event' }">New Event</RouterLink>
           </span>
       </nav>
   </header>
```

_Now show the staff when you are trying to login with a different user name,and password. What is the menu you have seen?_

Note that you can block the usage on the backend site by updating the SecurityConfiguration as given.

```
                       .requestMatchers(HttpMethod.OPTIONS,"/**").permitAll()
                       .requestMatchers(HttpMethod.POST,"/events").hasRole("ADMIN")
                       .anyRequest().authenticated();
```

9. Provide the register page which will register a new user to the system
