
(ns app.vehicle-models.frontend.editor.effects
    (:require [app.vehicle-models.frontend.editor.views :as editor.views]
              [re-frame.api                             :as r]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :vehicle-models.editor/load-editor!
  {:dispatch-n [[:x.gestures/init-view-handler! :vehicle-models.editor
                                                {:default-view-id :data}]
                [:vehicle-models.editor/render-editor!]]})

(r/reg-event-fx :vehicle-models.editor/render-editor!
  [:x.ui/render-surface! :vehicle-models.editor/view
                         {:content #'editor.views/view}])
