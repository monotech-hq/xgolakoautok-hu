
(ns app.contents.frontend.editor.effects
    (:require [app.contents.frontend.editor.views :as editor.views]
              [re-frame.api                       :as r]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :contents.editor/load-editor!
  {:dispatch-n [[:x.gestures/init-view-handler! :contents.editor
                                                {:default-view-id :data}]
                [:x.ui/render-surface! :contents.editor/view
                                       {:content #'editor.views/view}]]})
