
(ns app.contents.backend.handler.resolvers
    (:require [app.contents.backend.handler.helpers  :as handler.helpers]
              [com.wsscode.pathom3.connect.operation :refer [defresolver]]
              [pathom.api                            :as pathom]
              [x.user.api                            :as x.user]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-content-body-f
  ; @param (map) env
  ;  {:request (map)}
  ; @param (map) resolver-props
  ;
  ; @return (string)
  [{:keys [request] :as env} _]
  (if (x.user/request->authenticated? request)
      (let [content-id (pathom/env->param env :content-id)]
           (handler.helpers/get-content-body request {:content/id content-id}))))

(defresolver get-content-body
             ; @param (map) env
             ; @param (map) resolver-props
             ;  {:content-id (string)}
             ;
             ; @return (map)
             ;  {:contents.handler/get-content-body (string)}
             [env resolver-props]
             {:contents.handler/get-content-body (get-content-body-f env resolver-props)})

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (functions in vector)
(def HANDLERS [get-content-body])

(pathom/reg-handlers! ::handlers HANDLERS)
