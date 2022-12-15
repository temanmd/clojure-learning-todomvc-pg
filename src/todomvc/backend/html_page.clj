(ns todomvc.backend.html-page
  (:require
    [hiccup.page :refer [include-js
                         include-css
                         html5]]))

(defn html-page []
  (html5
    [:head
     [:meta {:charset "utf-8"}]
     [:meta {:name "viewport"
             :content "width=device-width, initial-scale=1"}]
     (include-css "https://cdn.jsdelivr.net/npm/bootstrap@5.2.2/dist/css/bootstrap.min.css")]
    [:body
     [:div#app.container.border]
     (include-js "/js/app.js")]))
