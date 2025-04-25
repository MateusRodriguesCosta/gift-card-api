# API Specification

## Overview

This API manages gift cards, handling creation, balance updates, cancellation, balance exchanges, and bulk operations. The API communicates using JSON and follows a RESTful style.

## Base URL

http://localhost:8080/api/v1

## Authentication

Users must first register via `/auth/register`, then log in at `/auth/login` to receive a generated HS256 token.
By including this token in the `Authorization` header, users can access the `/api/v1/**` endpoints as authenticated clients.  

## Notes

- All operations require valid card codes.
- Luhn algorithm used for secure code generation.