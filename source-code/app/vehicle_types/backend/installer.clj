
(ns app.vehicle-types.backend.installer
    (:require [app.schemes.backend.api :as schemes]
              [x.core.api              :as x.core]
              [x.user.api              :as x.user]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (namespaced maps in vector)
(def INITIAL-FIELDS [{:field/field-id :outer-width
                      :field/name :outer-width
                      :field/protected? true
                      :field/unit "mm"}
                     {:field/field-id :outer-length
                      :field/name :outer-length
                      :field/protected? true
                      :field/unit "mm"}
                     {:field/field-id :outer-height
                      :field/name :outer-height
                      :field/protected? true
                      :field/unit "mm"}
                     {:field/field-id :inner-width
                      :field/name :inner-width
                      :field/protected? true
                      :field/unit "mm"}
                     {:field/field-id :inner-length
                      :field/name :inner-length
                      :field/protected? true
                      :field/unit "mm"}
                     {:field/field-id :inner-height
                      :field/name :inner-height
                      :field/protected? true
                      :field/unit "mm"}
                     {:field/field-id :total-weight
                      :field/name :total-weight
                      :field/protected? true
                      :field/unit "kg"}
                     {:field/field-id :empty-weight
                      :field/name :empty-weight
                      :field/protected? true
                      :field/unit "kg"}
                     {:field/field-id :number-of-axis
                      :field/name :number-of-axis
                      :field/protected? true}
                     {:field/field-id :wheel-size
                      :field/name :wheel-size
                      :field/protected? true
                      :field/unit "\""}
                     {:field/field-id :tyre-size
                      :field/name :tyre-size
                      :field/protected? true}])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- installer
  ; @return (namespaced map)
  []
  (let [request {:session x.user/SYSTEM-USER-ACCOUNT}
        scheme  {:scheme/fields INITIAL-FIELDS}]
       (schemes/save-form! request :vehicle-types.technical-data scheme)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(x.core/reg-installer! ::installer installer)
