
(ns app.components.frontend.consent-dialog.effects
    (:require [app.components.frontend.consent-dialog.views :as consent-dialog.views]
              [re-frame.api                                 :as r]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :components.consent-dialog/render-dialog!
  ; @param (keyword)(opt) dialog-id
  ; @param (map) dialog-props
  ;  {:content (metamorphic-content)
  ;   :label (metamorphic-content)(opt)
  ;   :primary-button (map)(opt)
  ;   :secondary-button (map)(opt)}
  [r/event-vector<-id]
  (fn [{:keys [db]} [_ dialog-id dialog-props]]
      [:x.ui/render-popup! :components.consent-dialog/view
                           {:content [consent-dialog.views/component dialog-id dialog-props]}]))
