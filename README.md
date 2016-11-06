# pwa-clojure

This is a sample PWA app written n clojure + clojurescript.

It currently uses

* lein-cljsbuild
* lein ring (and therefore jetty 7.x)
* bidi

I'm hoping to move this to

* rum
* http-kit or jet

## Prod ready

There is lots of scope for expansion, especially fingerprinting assets, with a manifest, etc...

## Talk outline

* What is PWA
* Isomorphic Patterns
* Reader Conditionals
* Core Async
* Types of rendering
 * Server side
 * Page -> Page (load first, then component)
 * PWA -> Paga (can be done the same)
* Service Workers in Clojure
* (view-for :handler), (data-for :handler)
