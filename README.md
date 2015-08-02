# immutable-args
Cleaner, more functional implementation of Uncle Bob's Args program in Clean Code, page 194.
I found his example to be far too verbose and non-functional for my liking, so I figured I'd rewrite it using Java 8.

[![Build Status](https://travis-ci.org/Winwardo/immutable-args.svg)](https://travis-ci.org/Winwardo/immutable-args)
[![Coverage Status](https://coveralls.io/repos/Winwardo/immutable-args/badge.svg?branch=master&service=github)](https://coveralls.io/github/Winwardo/immutable-args?branch=master)

### Optional
Instead of throwing exceptions for missing values, I've opted for returning Optional<T> results, so the client may decide to handle missing values in a more graceful manner.  

### Immutable
I wanted to make it 100% immutable, but with as few dependencies as possible. Unfortunately there are no default ImmutableMaps in Java, so ArgsImmutable uses a HashMap under the hood. Once constructed however, Args is entirely read-only.

### Zero nulls
Unlike the original example, immutable-args does not use null at all - any NullPointerExceptions therefore are entirely the fault of anybody using it. This makes debugging that little bit easier.  
