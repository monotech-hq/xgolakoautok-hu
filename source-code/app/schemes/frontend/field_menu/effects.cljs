
(ns app.schemes.frontend.field-menu.effects
    (:require [app.schemes.frontend.form-handler.subs :as form-handler.subs]
              [re-frame.api                           :as r :refer [r]]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :schemes.field-menu/render-menu!
  ; @param (keyword) scheme-id
  ; @param (keyword) field-id
  (fn [{:keys [db]} [_ scheme-id field-id]]
      (let [{:field/keys [name]} (r form-handler.subs/get-scheme-field db scheme-id field-id)]
           [:components.context-menu/render-menu! :schemes.field-menu/view
                                                  {:label name
                                                   :menu-items [{:label :edit-field!   :on-click [:schemes.field-editor/load-editor!     scheme-id field-id]}
                                                                {:label :delete-field! :on-click [:schemes.field-deleter/render-consent! scheme-id field-id] :color :warning}]}])))
