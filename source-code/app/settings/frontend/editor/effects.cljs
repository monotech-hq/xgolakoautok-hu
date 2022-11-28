
(ns app.settings.frontend.editor.effects
    (:require [app.settings.frontend.editor.views :as editor.views]
              [re-frame.api                       :as r]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :settings.editor/load-editor!
  {:dispatch-n [[:x.gestures/init-view-handler! :settings.editor
                                                {:default-view-id :sales}]
                [:settings.editor/render-editor!]]})

(r/reg-event-fx :settings.editor/render-editor!
  [:x.ui/render-surface! :settings.editor/view
                         {:content #'editor.views/view}])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :settings.editor/user-settings-saved
  ; @param (map) saved-settings
  (fn [{:keys [db]} [_ saved-settings]]
      {:db (update-in db [:x.user :settings-handler/user-settings] merge saved-settings)}))
