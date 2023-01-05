(ns todomvc.shared.routes)

(def routes
  [["/api" {:interceptors [:api]}
    ["/todos" :todos]]
   ["/app" :app-index]])
