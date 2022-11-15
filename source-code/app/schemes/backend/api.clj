
(ns app.schemes.backend.api
    (:require [app.schemes.backend.field-deleter.mutations]
              [app.schemes.backend.field-editor.mutations]
              [app.schemes.backend.field-sorter.mutations]
              [app.schemes.backend.form-handler.resolvers]
              [app.schemes.backend.field-editor.side-effects :as field-editor.side-effects]
              [app.schemes.backend.form-handler.helpers      :as form-handler.helpers]
              [app.schemes.backend.form-handler.side-effects :as form-handler.side-effects]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; app.schemes.backend.field-editor.side-effects
(def save-field! field-editor.side-effects/save-field!)

; app.schemes.backend.form-handler.helpers
(def form-exists?  form-handler.helpers/form-exists?)
(def field-exists? form-handler.helpers/field-exists?)

; app.schemes.backend.form-handler.side-effects
(def save-form! form-handler.side-effects/save-form!)
