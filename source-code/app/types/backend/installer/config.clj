
(ns app.types.backend.installer.config)

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (namespaced maps in vector)
(def INITIAL-FIELDS [{:field/field-id :outer-width
                      :field/name :outer-width
                      :field/undeletable? true
                      :field/unit "mm"}
                     {:field/field-id :outer-length
                      :field/name :outer-length
                      :field/undeletable? true
                      :field/unit "mm"}
                     {:field/field-id :outer-height
                      :field/name :outer-height
                      :field/undeletable? true
                      :field/unit "mm"}
                     {:field/field-id :inner-width
                      :field/name :inner-width
                      :field/undeletable? true
                      :field/unit "mm"}
                     {:field/field-id :inner-length
                      :field/name :inner-length
                      :field/undeletable? true
                      :field/unit "mm"}
                     {:field/field-id :inner-height
                      :field/name :inner-height
                      :field/undeletable? true
                      :field/unit "mm"}
                     {:field/field-id :total-weight
                      :field/name :total-weight
                      :field/undeletable? true
                      :field/unit "kg"}
                     {:field/field-id :empty-weight
                      :field/name :empty-weight
                      :field/undeletable? true
                      :field/unit "kg"}
                     {:field/field-id :number-of-axis
                      :field/name :number-of-axis
                      :field/undeletable? true}
                     {:field/field-id :wheel-size
                      :field/name :wheel-size
                      :field/undeletable? true
                      :field/unit "\""}
                     {:field/field-id :tyre-size
                      :field/name :tyre-size
                      :field/undeletable? true}])
