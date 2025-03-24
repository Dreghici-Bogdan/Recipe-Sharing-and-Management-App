const express = require('express');
const bodyParser = require('body-parser');
const cors = require('cors');
const WebSocket = require('ws');
const http = require('http');

const app = express();
const PORT = 5000;

// Middleware
app.use(bodyParser.json());
app.use(cors());

const books = [
  {
    id: 1,
    title: "The Mystery of the Lost Island",
    author: "Arthur Cane",
    genre: "Mystery",
    year: 2021,
    isbn: "1234567890",
    availability: "available",
  },
  {
    id: 2,
    title: "Journey to the Wild West",
    author: "Caroline Harper",
    genre: "Action",
    year: 2020,
    isbn: "12345634350",
    availability: "checked out",
  },
  {
    id: 3,
    title: "The Secrets of the Universe",
    author: "Dr. Alan Green",
    genre: "Science",
    year: 2019,
    isbn: "9876543210",
    availability: "available",
  },
  {
    id: 4,
    title: "Cooking 101: Beginner Recipes",
    author: "Sarah Kitchen",
    genre: "Cooking",
    year: 2018,
    isbn: "5678901234",
    availability: "available",
  },
  {
    id: 5,
    title: "History of the World Wars",
    author: "Mark Stevenson",
    genre: "History",
    year: 2017,
    isbn: "4567890123",
    availability: "checked out",
  },
  {
    id: 6,
    title: "Tales of the Deep Sea",
    author: "Linda Shore",
    genre: "Adventure",
    year: 2022,
    isbn: "6789012345",
    availability: "available",
  },
  {
    id: 7,
    title: "Fantasy Realms: The Dragon King",
    author: "Evelyn Woods",
    genre: "Fantasy",
    year: 2019,
    isbn: "3456789012",
    availability: "checked out",
  },
  {
    id: 8,
    title: "The Last Stand",
    author: "Michael Blaze",
    genre: "Thriller",
    year: 2021,
    isbn: "7890123456",
    availability: "available",
  },
  {
    id: 9,
    title: "Gardening for Beginners",
    author: "Emily Bloom",
    genre: "Gardening",
    year: 2016,
    isbn: "9012345678",
    availability: "available",
  },
  {
    id: 10,
    title: "The Art of Meditation",
    author: "Zen Master Liu",
    genre: "Self-help",
    year: 2015,
    isbn: "8901234567",
    availability: "checked out",
  },
  {
    id: 11,
    title: "A Guide to Programming",
    author: "John Developer",
    genre: "Technology",
    year: 2020,
    isbn: "2345678901",
    availability: "available",
  },
  {
    id: 12,
    title: "Space Exploration: Past and Future",
    author: "Dr. Helen Astor",
    genre: "Science",
    year: 2018,
    isbn: "7654321098",
    availability: "checked out",
  },
  {
    id: 13,
    title: "Fitness Made Easy",
    author: "Jake Strong",
    genre: "Health",
    year: 2017,
    isbn: "8765432109",
    availability: "available",
  },
  {
    id: 14,
    title: "The Great Detective",
    author: "Sherlock Brown",
    genre: "Mystery",
    year: 2021,
    isbn: "1122334455",
    availability: "checked out",
  },
  {
    id: 15,
    title: "Romance in Paris",
    author: "Amelie Fontaine",
    genre: "Romance",
    year: 2022,
    isbn: "9988776655",
    availability: "available",
  },
];


// WebSocket setup
const server = http.createServer(app);
const wss = new WebSocket.Server({ server });

wss.on('connection', (ws) => {
  console.log('WebSocket client connected');
  ws.send('Server connected successfully!');
  ws.on('message', (message) => {
    console.log('Received message from client:', message);
  });
});

// Routes
const bookRouter = express.Router();

// Create a new book
bookRouter.post('/', (req, res) => {
  console.log('POST /api/books: Creating a new book');
  const { id, title, author, genre, year, isbn, availability } = req.body;

  if (!id || !title || !author || !genre || !year || !isbn || !availability) {
    console.error('POST /api/books: Invalid book data');
    return res.status(400).json({ message: 'All fields are required: title, author, genre, year, isbn, availability' });
  }

  const newBook = {
    id,
    title,
    author,
    genre,
    year,
    isbn,
    availability,
  };
  books.push(newBook);
  console.log('POST /api/books: Book created successfully', newBook);
  res.status(201).json(newBook);
});

// Get all books
bookRouter.get('/', (req, res) => {
  console.log('GET /api/books: Fetching all books');
  res.status(200).json(books);
});

// Get a single book by ID
bookRouter.get('/:id', (req, res) => {
  const bookId = parseInt(req.params.id);
  console.log(`GET /api/books/${bookId}: Fetching book details`);

  const book = books.find((b) => b.id === bookId);
  if (!book) {
    console.error(`GET /api/books/${bookId}: Book not found`);
    return res.status(404).json({ message: 'Book not found' });
  }

  console.log(`GET /api/books/${bookId}: Book fetched successfully`, book);
  res.status(200).json(book);
});

// Update a book by ID
bookRouter.put('/:id', (req, res) => {
  const bookId = parseInt(req.params.id);
  console.log(`PUT /api/books/${bookId}: Updating book`);

  const { title, author, genre, year, isbn, availability } = req.body;
  const bookIndex = books.findIndex((b) => b.id === bookId);

  if (bookIndex === -1) {
    console.error(`PUT /api/books/${bookId}: Book not found`);
    return res.status(404).json({ message: 'Book not found' });
  }

  if (!title || !author || !genre || !year || !isbn || !availability) {
    console.error('PUT /api/books: Invalid book data');
    return res.status(400).json({ message: 'All fields are required: title, author, genre, year, isbn, availability' });
  }

  const updatedBook = {
    id: books[bookIndex].id,
    title,
    author,
    genre,
    year,
    isbn,
    availability,
  };
  books[bookIndex] = updatedBook;
  console.log(`PUT /api/books/${bookId}: Book updated successfully`, updatedBook);
  res.status(200).json(updatedBook);
});

// Delete a book by ID
bookRouter.delete('/:id', (req, res) => {
  const bookId = parseInt(req.params.id);
  console.log(`DELETE /api/books/${bookId}: Deleting book`);

  const bookIndex = books.findIndex((b) => b.id === bookId);
  if (bookIndex === -1) {
    console.error(`DELETE /api/books/${bookId}: Book not found`);
    return res.status(404).json({ message: 'Book not found' });
  }

  books.splice(bookIndex, 1);
  console.log(`DELETE /api/books/${bookId}: Book deleted successfully`);
  res.status(200).json({ message: 'Book deleted successfully' });
});

// Use the book routes
app.use('/api/books', bookRouter);

// Start server
server.listen(PORT, () => {
  console.log(`Server running on http://localhost:${PORT}`);
});
