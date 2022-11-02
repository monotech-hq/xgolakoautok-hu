
(ns app.settings.frontend.editor.effects
    (:require [app.settings.frontend.editor.events :as editor.events]
              [app.settings.frontend.editor.views  :as editor.views]
              [re-frame.api                        :as r :refer [r]]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :settings.editor/load-editor!
  (fn [{:keys [db]} _]
      {:dispatch-n [[:gestures/init-view-handler! :settings.editor
                                                  {:default-view-id :sales}]
                    [:settings.editor/render-editor!]]}))

(r/reg-event-fx :settings.editor/render-editor!
  [:ui/render-surface! :settings.editor/view
                       {:content #'editor.views/view}])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :settings.editor/user-settings-saved
  (fn [{:keys [db]} [_ saved-settings]]
      {:db (update-in db [:user :settings-handler/user-settings] merge saved-settings)}))
