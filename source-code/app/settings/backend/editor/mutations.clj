
(ns app.settings.backend.editor.mutations
    (:require [app.common.backend.api                :as common]
              [com.wsscode.pathom3.connect.operation :as pathom.co :refer [defmutation]]
              [mongo-db.api                          :as mongo-db]
              [pathom.api                            :as pathom]
              [x.user.api                         :as x.user]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn save-item-f
  ; @param (map) env
  ;  {:request (map)}
  ; @param (map) mutation-props
  ;  {:item (namespaced map)}
  ;
  ; @return (namespaced map)
  [{:keys [request]} {:keys [item]}]
  ; XXX#9100 (app.settings.backend.editor.mutations)
  ; A felhasználói beállítások mentésekor a felhasználó azonosítójának forrása,
  ; NEM a paraméterként a szerver számára küldött beállításokat tartalmazó dokumentum,
  ; HANEM a request térkép session értékéből kiolvasott azonosító!
  (let [postpare-f     #(common/updated-document-prototype request %)
        user-account-id (x.user/request->user-account-id   request)]
       (letfn [(f [%] (merge % item))]
              (mongo-db/apply-document! "user_settings" user-account-id f {:postpare-f postpare-f}))))

(defmutation save-item!
             ; @param (map) env
             ; @param (map) mutation-props
             ;  {:item (namespaced map)}
             ;
             ; @return (namespaced map)
             [env mutation-props]
             {::pathom.co/op-name 'settings.editor/save-item!}
             (save-item-f env mutation-props))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (functions in vector)
(def HANDLERS [save-item!])

(pathom/reg-handlers! ::handlers HANDLERS)
