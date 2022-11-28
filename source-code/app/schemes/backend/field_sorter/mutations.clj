
(ns app.schemes.backend.field-sorter.mutations
    (:require [app.schemes.backend.form-handler.side-effects :as form-handler.side-effects]
              [com.wsscode.pathom3.connect.operation         :as pathom.co :refer [defmutation]]
              [mongo-db.api                                  :as mongo-db]
              [pathom.api                                    :as pathom]
              [x.user.api                                    :as x.user]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn reorder-fields-f
      ; @param (map) env
      ;  {:request (map)}
      ; @param (map) mutation-props
      ;  {:reordered-fields (namespaced maps in vector)
      ;   :scheme-id (keyword)}
      [{:keys [request]} {:keys [reordered-fields scheme-id]}]
      (if (x.user/request->authenticated? request)
          (if-let [scheme-item (mongo-db/get-document-by-query "schemes" {:scheme/scheme-id scheme-id})]
                  (let [updated-scheme (assoc scheme-item :scheme/fields reordered-fields)]
                       (form-handler.side-effects/save-form! request scheme-id updated-scheme)))))

(defmutation reorder-fields!
             ; @param (map) env
             ; @param (map) mutation-props
             ;  {:reordered-fields (namespaced maps in vector)
             ;   :scheme-id (keyword)}
             [env mutation-props]
             {::pathom.co/op-name 'schemes.field-sorter/reorder-fields!}
             (reorder-fields-f env mutation-props))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (functions in vector)
(def HANDLERS [reorder-fields!])

(pathom/reg-handlers! ::handlers HANDLERS)
