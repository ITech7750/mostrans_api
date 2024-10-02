# Base Node Image
FROM node:14 as build-stage

# Set the working directory
WORKDIR /app

# Copy package.json and package-lock.json files
COPY package*.json /app/

# Install dependencies
RUN npm install

# Copy the rest of the application code
COPY ./ /app/

# Build the application
RUN npm run build  # assuming this is the webpack build command

# Expose the port your app runs on
EXPOSE 8000

# Run the application
CMD ["npm", "start"]  # You can replace this with your actual start command

