
(ns app.schemes.backend.field-editor.mutations
    (:require [app.schemes.backend.field-editor.prototypes   :as field-editor.prototypes]
              [app.schemes.backend.field-editor.side-effects :as field-editor.side-effects]
              [com.wsscode.pathom3.connect.operation         :as pathom.co :refer [defmutation]]
              [pathom.api                                    :as pathom]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn save-field-f
      ; @param (map) env
      ;  {:request (map)}
      ; @param (map) mutation-props
      ;  {:field-item (namespaced map)
      ;   :scheme-id (keyword)}
      [{:keys [request]} {:keys [field-item scheme-id]}]
      (let [field-item (field-editor.prototypes/field-item-prototype field-item)]
           (field-editor.side-effects/save-field! request scheme-id field-item)))

(defmutation save-field!
             ; @param (map) env
             ; @param (map) mutation-props
             ;  {:field-item (namespaced map)
             ;   :scheme-id (keyword)}
             [env mutation-props]
             {::pathom.co/op-name 'schemes.field-editor/save-field!}
             (save-field-f env mutation-props))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (functions in vector)
(def HANDLERS [save-field!])

(pathom/reg-handlers! ::handlers HANDLERS)
