📱 #Recipe Sharing and Management App
A mobile application that allows users to upload, view, and manage recipes while exploring recipes shared by others. The app works both online and offline, ensuring users can access their saved recipes at any time.

📌 ##Main Section
View all recipes – Retrieve and display a list of recipes using GET /recipes. If offline, show an offline message and allow retrying. Once retrieved, data remains accessible offline;
View detailed recipe information – Fetch full details of a recipe using GET /recipe. Once retrieved, data remains accessible offline;
Add a new recipe – Upload a recipe via POST /recipe. Requires internet connectivity; 
Edit or delete a recipe – Modify or remove a recipe using DELETE /recipe. Requires internet connectivit;

🛠️ ##Additional Features
Progress indicator – Display a loading spinner during server operations.
Error handling – Show errors using a Toast or Snackbar. Log all server and database interactions. 

🗄️ ##Server-Side Data Structure
Each recipe consists of the following details:

| Field           | Type                | Description                      |
|-----------------|---------------------|----------------------------------|
| **id**          | Integer (>0)        | Unique identifier for the recipe |
| **date**        | String (YYYY-MM-DD) | Date when the recipe was created |
| **title**       | String              | Recipe title                     |
| **ingredients** | String              | List of ingredients used         |
| **category**    | String              | Recipe category                  |
| **rating**      | Decimal             | Average rating of the recipe     |
