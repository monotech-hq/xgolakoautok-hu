
(ns app.settings.backend.handler.mutations
    (:require [app.common.backend.api                :as common]
              [com.wsscode.pathom3.connect.operation :as pathom.co :refer [defmutation]]
              [mongo-db.api                          :as mongo-db]
              [pathom.api                            :as pathom]
              [x.server-user.api                     :as x.user]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn update-user-settings-f
  ; @param (map) env
  ;  {:request (map)}
  ; @param (map) mutation-props
  ;  {:user-settings (namespaced map)}
  ;
  ; @return (namespaced map)
  [{:keys [request]} {:keys [user-settings]}]
  ; XXX#9100 (app.settings.backend.editor.mutations)
  (let [postpare-f     #(common/updated-document-prototype request %)
        user-account-id (x.user/request->user-account-id   request)]
       (mongo-db/apply-document! "user_settings" user-account-id #(merge % user-settings) {:postpare-f postpare-f})))

(defmutation update-user-settings!
             ; @param (map) env
             ; @param (map) mutation-props
             ;  {:user-settings (namespaced map)}
             ;
             ; @return (namespaced map)
             [env mutation-props]
             {::pathom.co/op-name 'settings.handler/update-user-settings!}
             (update-user-settings-f env mutation-props))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (functions in vector)
(def HANDLERS [update-user-settings!])

(pathom/reg-handlers! ::handlers HANDLERS)
