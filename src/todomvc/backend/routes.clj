(ns todomvc.backend.routes
  (:require
    [reitit.ring :as ring]
    [reitit.core :as ring-core]
    [reitit.coercion.schema]
    [reitit.ring.coercion :refer [coerce-exceptions-middleware
                                  coerce-request-middleware
                                  coerce-response-middleware]]
    [reitit.ring.middleware.exception :refer [exception-middleware]]
    [reitit.ring.middleware.muuntaja :refer [format-request-middleware
                                             format-response-middleware
                                             format-negotiate-middleware]]
    [muuntaja.core :as m]
    [todomvc.backend.router-handlers :as handlers]
    [todomvc.shared.routes :as shared-routes]
    [clojure.pprint :refer [pprint]]))

(defn api-handler [request]
  {:status 200
   :headers {"Content-Type" "text/html"}
   :body "Hello, World! Its API."})

; (defn my-expand
;   [data opts]
;   (if (keyword? data)
;     (case data
;       :todos {:handler api-handler}
;       {:handler frontend-handler})
;     (ring-core/expand data opts)))

; (defn my-expand [registry]
;   (fn [data opts]
;     (if (keyword? data)
;       (some-> data
;         registry
;         (ring-core/expand opts)
;         (assoc :name data))
;       (ring-core/expand data opts))))

(def router
  (ring/ring-handler
    (ring/router
      shared-routes/routes
      {:data {:coercion reitit.coercion.schema/coercion
              :muuntaja m/instance
              :middleware [format-negotiate-middleware
                           format-response-middleware
                           exception-middleware
                           format-request-middleware
                           coerce-exceptions-middleware
                           coerce-request-middleware
                           coerce-response-middleware]}})
    (ring/routes
      (ring/create-resource-handler {:path "/" :root "/public"})
      (ring/create-default-handler
        {:not-found (constantly {:status 404 :body "Not found"})}))))

(comment
  (pprint shared-routes/routes)
  (pprint {:a 1 :b 2}))
