
(ns app.schemes.frontend.field-menu.effects
    (:require [app.schemes.frontend.field-menu.views :as field-menu.views]
              [re-frame.api                          :as r]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :schemes.field-menu/render-menu!
  ; @param (keyword) scheme-id
  ; @param (keyword) field-id
  (fn [_ [_ scheme-id field-id]]
      [:ui/render-popup! :schemes.field-menu/view
                         {:content [field-menu.views/view scheme-id field-id]}]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :schemes.field-menu/edit-field!
  ; @param (keyword) scheme-id
  ; @param (keyword) field-id
  (fn [_ [_ scheme-id field-id]]
      {:dispatch-n [[:ui/remove-popup! :schemes.field-menu/view]
                    [:schemes.field-editor/load-editor! scheme-id field-id]]}))

(r/reg-event-fx :schemes.field-menu/delete-field!
  ; @param (keyword) scheme-id
  ; @param (keyword) field-id
  (fn [_ [_ scheme-id field-id]]
      {:dispatch-n [[:ui/remove-popup! :schemes.field-menu/view]
                    [:schemes.field-deleter/render-consent! scheme-id field-id]]}))
