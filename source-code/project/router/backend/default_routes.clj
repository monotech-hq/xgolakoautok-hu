
(ns project.router.backend.default-routes
    (:require [project.ui.backend.api :as ui]
              [server-fruits.http     :as http]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (function)
;  No method matched
(def METHOD-NOT-ALLOWED #(http/html-wrap {:body (ui/main %) :status 404}))

; @constant (function)
;  Handler returned nil
(def NOT-ACCEPTABLE #(http/html-wrap {:body (ui/main %) :status 404}))

; @constant (function)
;  No route matched – {:status 200} handled at client-side
(def NOT-FOUND #(http/html-wrap {:body (ui/main %) :status 200}))
