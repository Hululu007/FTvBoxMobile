"""Verify that portrait layouts retain every view used by the reference logic."""
from pathlib import Path
import json
import xml.etree.ElementTree as ET

ROOT = Path(__file__).resolve().parents[1]
RES = ROOT / "app/src/main/res"
ANDROID = "{http://schemas.android.com/apk/res/android}"


def ids(path):
    return {
        element.attrib[ANDROID + "id"].split("/")[-1]: element.tag
        for element in ET.parse(path).getroot().iter()
        if ANDROID + "id" in element.attrib
    }


# The single-Activity host mounts PlaybackSection directly, without a Fragment container.
screen_container_types = {("activity_detail.xml", "previewPlayer"): "FrameLayout"}
baseline = json.loads((ROOT / "tools/reference_layout_contracts.json").read_text())["layouts"]
for filename, original_ids in baseline.items():
    for variant in ("layout", "layout-port"):
        path = RES / variant / filename
        if variant == "layout-port" and not path.exists():
            # Android uses the default resource when no portrait override is defined.
            path = RES / "layout" / filename
        assert path.exists(), f"Missing layout: {path}"
        phone_ids = ids(path)
        for name, view_type in original_ids.items():
            # The mobile bottom sheet sizes the episode list without the TV half-screen guide.
            if (filename, name) == ("dialog_episode.xml", "episode_half"):
                assert name not in phone_ids, (variant, filename, name)
                continue
            expected_type = screen_container_types.get((filename, name), view_type)
            assert phone_ids.get(name) == expected_type, (
                variant, filename, name, expected_type, phone_ids.get(name)
            )
print(f"All {len(baseline)} layouts and their portrait overrides/default fallbacks preserve upstream IDs and expected view types, "
      "including the explicit PlaybackSection container replacement.")
