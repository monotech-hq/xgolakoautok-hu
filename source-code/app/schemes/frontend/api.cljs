
(ns app.schemes.frontend.api
    (:require [app.schemes.frontend.field-editor.effects]
              [app.schemes.frontend.field-editor.events]
              [app.schemes.frontend.field-menu.effects]
              [app.schemes.frontend.field-deleter.effects]
              [app.schemes.frontend.field-deleter.events]
              [app.schemes.frontend.field-sorter.effects]
              [app.schemes.frontend.field-sorter.events]
              [app.schemes.frontend.form-handler.subs]
              [app.schemes.frontend.lifecycles]
              [app.schemes.frontend.form-handler.queries  :as form-handler.queries]
              [app.schemes.frontend.scheme-controls.views :as scheme-controls.views]
              [app.schemes.frontend.scheme-data.views     :as scheme-data.views]
              [app.schemes.frontend.scheme-form.views     :as scheme-form.views]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; app.schemes.frontend.form-handler.helpers
(def request-scheme-form-query form-handler.queries/request-scheme-form-query)

; app.schemes.frontend.scheme-controls.views
(def scheme-controls scheme-controls.views/view)

; app.schemes.frontend.scheme-data.views
(def scheme-data scheme-data.views/view)

; app.schemes.frontend.scheme-form.views
(def scheme-form scheme-form.views/view)
