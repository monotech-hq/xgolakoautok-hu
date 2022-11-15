
(ns app.website-post.backend.handler.mutations
    (:require [app.website-post.backend.handler.side-effects :as handler.side-effects]
              [com.wsscode.pathom3.connect.operation         :as pathom.co :refer [defmutation]]
              [pathom.api                                    :as pathom]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn send-message-f
  ; @param (map) env
  ; @param (map) mutation-props
  ;  {:visitor-email-address (string)
  ;   :visitor-message (string)
  ;   :visitor-name (string)}
  [{:keys [request]} mutation-props]
  (handler.side-effects/send-message! request mutation-props))

(defmutation send-message!
             ; @param (map) env
             ; @param (map) mutation-props
             ;  {:visitor-email-address (string)
             ;   :visitor-message (string)
             ;   :visitor-name (string)}
             ;
             ; @return (namespaced map)
             [env mutation-props]
             {::pathom.co/op-name 'website-post.handler/send-message!}
             (send-message-f env mutation-props))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (functions in vector)
(def HANDLERS [send-message!])

(pathom/reg-handlers! ::handlers HANDLERS)
